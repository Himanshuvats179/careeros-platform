package com.careeros.auth.service;

import com.careeros.auth.dto.event.AuthEvent;
import com.careeros.auth.dto.request.LoginRequest;
import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.AuthResponse;
import com.careeros.auth.entity.RefreshToken;
import com.careeros.auth.entity.User;
import com.careeros.auth.enums.Role;
import com.careeros.auth.exception.UserAlreadyExistsException;
import com.careeros.auth.mapper.AuthMapper;
import com.careeros.auth.repository.RefreshTokenRepository;
import com.careeros.auth.repository.UserRepository;
import com.careeros.auth.security.JwtTokenProvider;
import com.careeros.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private KafkaTemplate<String, AuthEvent> kafkaTemplate;

    @InjectMocks
    private AuthServiceImpl authService;

    private UUID userId;
    private User user;
    private RegisterRequest registerRequest;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .email("alex.rivera@careeros.com")
                .password("hashed_password")
                .firstName("Alex")
                .lastName("Rivera")
                .enabled(true)
                .roles(Set.of(Role.ROLE_CANDIDATE))
                .build();

        registerRequest = RegisterRequest.builder()
                .email("alex.rivera@careeros.com")
                .password("Password123!")
                .firstName("Alex")
                .lastName("Rivera")
                .roles(Set.of(Role.ROLE_CANDIDATE))
                .build();

        refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID())
                .user(user)
                .token("mock-refresh-token")
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
    }

    @Test
    @DisplayName("Should register new user and publish Kafka event")
    void register_Success() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(UUID.class), anyString(), any())).thenReturn("mock-access-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock-access-token", response.getAccessToken());
        assertEquals("alex.rivera@careeros.com", response.getEmail());
        verify(userRepository).save(any(User.class));
        verify(kafkaTemplate).send(eq("careeros.auth.events"), anyString(), any(AuthEvent.class));
    }

    @Test
    @DisplayName("Should throw exception when registering existing email")
    void register_UserAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequest));
    }
}
