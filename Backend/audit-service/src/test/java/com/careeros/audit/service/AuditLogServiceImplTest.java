package com.careeros.audit.service;

import com.careeros.audit.dto.event.AuditEvent;
import com.careeros.audit.dto.request.AuditCreateRequest;
import com.careeros.audit.dto.response.AuditLogResponse;
import com.careeros.audit.entity.AuditLog;
import com.careeros.audit.mapper.AuditLogMapper;
import com.careeros.audit.repository.AuditLogRepository;
import com.careeros.audit.service.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    private UUID eventId;
    private UUID userId;
    private UUID logId;
    private AuditLog auditLog;
    private AuditEvent auditEvent;
    private AuditLogResponse auditLogResponse;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        userId = UUID.randomUUID();
        logId = UUID.randomUUID();

        auditLog = AuditLog.builder()
                .id(logId)
                .eventId(eventId)
                .userId(userId)
                .eventType("USER_REGISTERED")
                .serviceName("AUTH_SERVICE")
                .action("User account created")
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .build();

        auditEvent = AuditEvent.builder()
                .eventId(eventId)
                .userId(userId)
                .eventType("USER_REGISTERED")
                .serviceName("AUTH_SERVICE")
                .action("User account created")
                .build();

        auditLogResponse = AuditLogResponse.builder()
                .id(logId)
                .eventId(eventId)
                .userId(userId)
                .eventType("USER_REGISTERED")
                .serviceName("AUTH_SERVICE")
                .action("User account created")
                .status("SUCCESS")
                .build();
    }

    @Test
    @DisplayName("Should process Kafka event idempotently when event is new")
    void processKafkaEvent_Success() {
        when(auditLogRepository.existsByEventId(eventId)).thenReturn(false);
        when(auditLogMapper.toEntity(auditEvent)).thenReturn(auditLog);

        auditLogService.processKafkaEvent(auditEvent);

        verify(auditLogRepository).save(auditLog);
    }

    @Test
    @DisplayName("Should skip processing duplicate Kafka event ID (Idempotency)")
    void processKafkaEvent_DuplicateSkipped() {
        when(auditLogRepository.existsByEventId(eventId)).thenReturn(true);

        auditLogService.processKafkaEvent(auditEvent);

        verify(auditLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve audit log by ID successfully")
    void getAuditLogById_Success() {
        when(auditLogRepository.findById(logId)).thenReturn(Optional.of(auditLog));
        when(auditLogMapper.toResponse(auditLog)).thenReturn(auditLogResponse);

        AuditLogResponse response = auditLogService.getAuditLogById(logId);

        assertNotNull(response);
        assertEquals(logId, response.getId());
        assertEquals("USER_REGISTERED", response.getEventType());
    }
}
