package com.careeros.auth.service.impl;

import com.careeros.auth.dto.event.AuthEvent;
import com.careeros.auth.dto.request.LoginRequest;
import com.careeros.auth.dto.request.RefreshTokenRequest;
import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.AuthResponse;
import com.careeros.auth.dto.response.UserResponse;
import com.careeros.auth.entity.RefreshToken;
import com.careeros.auth.entity.User;
import com.careeros.auth.enums.Role;
import com.careeros.auth.exception.AuthServiceException;
import com.careeros.auth.exception.UserAlreadyExistsException;
import com.careeros.auth.mapper.AuthMapper;
import com.careeros.auth.repository.RefreshTokenRepository;
import com.careeros.auth.repository.UserRepository;
import com.careeros.auth.security.JwtTokenProvider;
import com.careeros.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String KAFKA_TOPIC = "careeros.auth.events";

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, AuthEvent> kafkaTemplate;

    public AuthServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, AuthMapper authMapper, @Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate, KafkaTemplate<String, AuthEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.authMapper = authMapper;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering user email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User email already registered: " + request.getEmail());
        }

        Set<Role> userRoles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            userRoles.addAll(request.getRoles());
        } else {
            userRoles.add(Role.ROLE_CANDIDATE);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .roles(userRoles)
                .build();

        User savedUser = userRepository.save(user);

        Set<String> roleNames = savedUser.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String accessToken = jwtTokenProvider.generateToken(savedUser.getId(), savedUser.getEmail(), roleNames);
        RefreshToken refreshToken = createRefreshToken(savedUser);

        // Publish USER_REGISTERED event to Kafka
        publishKafkaEvent("USER_REGISTERED", savedUser.getId(), "User account registered: " + savedUser.getEmail(), savedUser.getId().toString());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .roles(roleNames)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Authenticating user email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthServiceException("User not found after authentication: " + request.getEmail()));

        Set<String> roleNames = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), roleNames);
        RefreshToken refreshToken = createRefreshToken(user);

        // Publish USER_LOGGED_IN event to Kafka
        publishKafkaEvent("USER_LOGGED_IN", user.getId(), "User authentication successful for: " + user.getEmail(), user.getId().toString());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Processing refresh token request");

        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AuthServiceException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new AuthServiceException("Refresh token expired. Please sign in again.");
        }

        User user = token.getUser();
        Set<String> roleNames = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String newAccessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), roleNames);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(token.getToken())
                .userId(user.getId())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }

    @Override
    @Transactional
    public void logout(String accessToken, String refreshToken) {
        log.info("Logging out user token");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            // Blacklist JWT in Redis for remaining lifetime (24 hours)
            redisTemplate.opsForValue().set("blacklist:" + token, "logout", 24, TimeUnit.HOURS);
        }

        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthServiceException("User profile not found with ID: " + userId));
        return authMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        log.info("Changing password for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthServiceException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthServiceException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        publishKafkaEvent("PASSWORD_CHANGED", userId, "Password changed for user: " + user.getEmail(), userId.toString());
    }

    private RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(7 * 24 * 60 * 60 * 1000L)) // 7 days expiration
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private void publishKafkaEvent(String eventType, UUID userId, String action, String responseData) {
        try {
            AuthEvent event = AuthEvent.builder()
                    .userId(userId)
                    .eventType(eventType)
                    .action(action)
                    .responseData(responseData)
                    .build();
            kafkaTemplate.send(KAFKA_TOPIC, event.getEventId().toString(), event);
        } catch (Exception e) {
            log.error("Failed to publish Kafka auth event for action {}: {}", action, e.getMessage());
        }
    }
}