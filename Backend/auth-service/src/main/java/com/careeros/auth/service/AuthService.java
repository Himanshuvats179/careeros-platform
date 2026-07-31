package com.careeros.auth.service;

import com.careeros.auth.dto.request.LoginRequest;
import com.careeros.auth.dto.request.RefreshTokenRequest;
import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.AuthResponse;
import com.careeros.auth.dto.response.UserResponse;

import java.util.UUID;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(String accessToken, String refreshToken);
    UserResponse getCurrentUser(UUID userId);
    void changePassword(UUID userId, String currentPassword, String newPassword);
}