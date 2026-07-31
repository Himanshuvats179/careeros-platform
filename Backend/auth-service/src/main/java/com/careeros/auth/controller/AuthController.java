package com.careeros.auth.controller;

import com.careeros.auth.dto.request.LoginRequest;
import com.careeros.auth.dto.request.RefreshTokenRequest;
import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.AuthResponse;
import com.careeros.auth.dto.response.UserResponse;
import com.careeros.auth.service.AuthService;
import com.careeros.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication & Authorization", description = "APIs for user registration, JWT authentication, refresh token rotation, and RBAC security.")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Registers a new user account, hashes password using BCrypt, and returns JWT tokens.")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("REST request to register user: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates user credentials and returns access & refresh JWT tokens.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("REST request to login user: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("User authenticated successfully", response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT Token", description = "Generates a new JWT access token using a valid refresh token.")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("REST request to refresh access token");
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Invalidates the access token in Redis blacklist and deletes refresh token.")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestParam(required = false) String refreshToken) {
        log.info("REST request to logout user");
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.success("User logged out successfully"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get Current User Profile", description = "Retrieves current authenticated user details using X-User-Id header.")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@RequestHeader("X-User-Id") UUID userId) {
        log.info("REST request to get current user details for ID: {}", userId);
        UserResponse response = authService.getCurrentUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Current user profile retrieved successfully", response));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change Password", description = "Allows authenticated user to change their password by verifying current password first.")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody Map<String, String> request) {
        log.info("REST request to change password for user ID: {}", userId);
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        authService.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
}
