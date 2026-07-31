package com.careeros.notification.controller;

import com.careeros.notification.dto.request.NotificationCreateRequest;
import com.careeros.notification.dto.response.NotificationResponse;
import com.careeros.notification.enums.NotificationStatus;
import com.careeros.notification.enums.NotificationType;
import com.careeros.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private UUID notifId;
    private UUID recipientId;
    private NotificationResponse response;

    @BeforeEach
    void setUp() {
        notifId = UUID.randomUUID();
        recipientId = UUID.randomUUID();

        response = NotificationResponse.builder()
                .id(notifId)
                .recipientId(recipientId)
                .title("Interview Scheduled")
                .message("Your interview is set for Friday at 10 AM.")
                .type(NotificationType.IN_APP)
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/notifications - Should create notification and return 201 Created")
    void createNotification_ShouldReturn201() throws Exception {
        NotificationCreateRequest request = NotificationCreateRequest.builder()
                .recipientId(recipientId)
                .title("Interview Scheduled")
                .message("Your interview is set for Friday at 10 AM.")
                .type(NotificationType.IN_APP)
                .build();

        when(notificationService.createNotification(any(NotificationCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Interview Scheduled"));
    }
}
