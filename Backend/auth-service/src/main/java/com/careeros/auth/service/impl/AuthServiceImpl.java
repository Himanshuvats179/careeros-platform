package com.careeros.auth.service.impl;

import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.RegisterResponse;
import com.careeros.auth.entity.Role;
import com.careeros.auth.entity.User;
import com.careeros.common.exception.BadRequestException;
import com.careeros.common.exception.ResourceNotFoundException;
import com.careeros.auth.repository.RoleRepository;
import com.careeros.auth.repository.UserRepository;
import com.careeros.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);

        log.info("Registration request received for email={}", normalizedEmail);

        if (userRepository.existsByEmail(normalizedEmail)) {
            log.warn("Registration rejected because email already exists: email={}", normalizedEmail);
            throw new BadRequestException("An account already exists with this email");
        }

        Role userRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> {
                    log.error("Required default role is missing: role={}", ROLE_USER);
                    return new ResourceNotFoundException("Default role ROLE_USER is missing");
                });

        User user = User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName().trim())
                .lastName(normalizeOptional(request.getLastName()))
                .roles(Set.of(userRole))
                .enabled(true)
                .emailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        log.info(
                "User registered successfully: userId={}, email={}, role={}",
                savedUser.getId(),
                savedUser.getEmail(),
                ROLE_USER
        );

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .message("Registration successful")
                .build();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}