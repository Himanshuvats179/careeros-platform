package com.careeros.audit.controller;

import com.careeros.audit.dto.request.AuditCreateRequest;
import com.careeros.audit.dto.response.AuditLogResponse;
import com.careeros.audit.service.AuditLogService;
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

@WebMvcTest(AuditLogController.class)
class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditLogService auditLogService;

    private UUID logId;
    private UUID eventId;
    private UUID userId;
    private AuditLogResponse response;

    @BeforeEach
    void setUp() {
        logId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        userId = UUID.randomUUID();

        response = AuditLogResponse.builder()
                .id(logId)
                .eventId(eventId)
                .userId(userId)
                .eventType("RESUME_UPLOADED")
                .serviceName("PROFILE_SERVICE")
                .action("Uploaded resume PDF")
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/audit-logs - Should create audit log and return 201 Created")
    void createAuditLog_ShouldReturn201() throws Exception {
        AuditCreateRequest request = AuditCreateRequest.builder()
                .eventId(eventId)
                .userId(userId)
                .eventType("RESUME_UPLOADED")
                .serviceName("PROFILE_SERVICE")
                .action("Uploaded resume PDF")
                .build();

        when(auditLogService.createAuditLog(any(AuditCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.eventType").value("RESUME_UPLOADED"));
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/{id} - Should return audit log and 200 OK")
    void getAuditLogById_ShouldReturn200() throws Exception {
        when(auditLogService.getAuditLogById(logId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/audit-logs/{id}", logId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(logId.toString()));
    }
}
