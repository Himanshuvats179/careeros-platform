package com.careeros.profile.controller;

import com.careeros.profile.dto.request.ProfileCreateRequest;
import com.careeros.profile.dto.response.ProfileResponse;
import com.careeros.profile.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    private UUID userId;
    private UUID profileId;
    private ProfileResponse profileResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();

        profileResponse = ProfileResponse.builder()
                .id(profileId)
                .userId(userId)
                .firstName("Jane")
                .lastName("Smith")
                .headline("Lead AI Architect")
                .version(0L)
                .completionPercentage(85)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/profiles - Should create profile and return 201 Created")
    void createProfile_ShouldReturn201() throws Exception {
        ProfileCreateRequest request = ProfileCreateRequest.builder()
                .userId(userId)
                .firstName("Jane")
                .lastName("Smith")
                .headline("Lead AI Architect")
                .build();

        when(profileService.createProfile(any(ProfileCreateRequest.class))).thenReturn(profileResponse);

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Jane"))
                .andExpect(jsonPath("$.data.lastName").value("Smith"));
    }

    @Test
    @DisplayName("GET /api/v1/profiles/{id} - Should return profile and 200 OK")
    void getProfileById_ShouldReturn200() throws Exception {
        when(profileService.getProfileById(profileId)).thenReturn(profileResponse);

        mockMvc.perform(get("/api/v1/profiles/{id}", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(profileId.toString()))
                .andExpect(jsonPath("$.data.headline").value("Lead AI Architect"));
    }
}
