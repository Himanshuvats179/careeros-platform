package com.careeros.auth.controller;

import com.careeros.auth.dto.request.LoginRequest;
import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.AuthResponse;
import com.careeros.auth.enums.Role;
import com.careeros.auth.security.JwtTokenProvider;
import com.careeros.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private UUID userId;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        authResponse = AuthResponse.builder()
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .userId(userId)
                .email("alex.rivera@careeros.com")
                .roles(Set.of("ROLE_CANDIDATE"))
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should register user and return 201 Created")
    void register_ShouldReturn201() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("alex.rivera@careeros.com")
                .password("Password123!")
                .firstName("Alex")
                .lastName("Rivera")
                .roles(Set.of(Role.ROLE_CANDIDATE))
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("mock-access-token"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should authenticate user and return 200 OK")
    void login_ShouldReturn200() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("alex.rivera@careeros.com")
                .password("Password123!")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("alex.rivera@careeros.com"));
    }
}
