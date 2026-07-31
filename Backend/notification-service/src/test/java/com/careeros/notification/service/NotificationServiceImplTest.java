package com.careeros.notification.service;

import com.careeros.notification.dto.event.NotificationEvent;
import com.careeros.notification.dto.request.NotificationCreateRequest;
import com.careeros.notification.dto.response.NotificationResponse;
import com.careeros.notification.entity.NotificationLog;
import com.careeros.notification.enums.NotificationStatus;
import com.careeros.notification.enums.NotificationType;
import com.careeros.notification.mapper.NotificationMapper;
import com.careeros.notification.repository.NotificationLogRepository;
import com.careeros.notification.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private UUID notifId;
    private UUID recipientId;
    private UUID eventId;
    private NotificationLog entity;
    private NotificationResponse response;
    private NotificationCreateRequest request;

    @BeforeEach
    void setUp() {
        notifId = UUID.randomUUID();
        recipientId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        request = NotificationCreateRequest.builder()
                .recipientId(recipientId)
                .title("Application Submitted")
                .message("Your application for Senior Architect at Google was received.")
                .type(NotificationType.IN_APP)
                .eventId(eventId)
                .build();

        entity = NotificationLog.builder()
                .id(notifId)
                .recipientId(recipientId)
                .title("Application Submitted")
                .message("Your application for Senior Architect at Google was received.")
                .type(NotificationType.IN_APP)
                .status(NotificationStatus.SENT)
                .eventId(eventId)
                .build();

        response = NotificationResponse.builder()
                .id(notifId)
                .recipientId(recipientId)
                .title("Application Submitted")
                .type(NotificationType.IN_APP)
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create notification log and return response")
    void createNotification_Success() {
        when(notificationLogRepository.save(any(NotificationLog.class))).thenReturn(entity);
        when(notificationMapper.toResponse(entity)).thenReturn(response);

        NotificationResponse result = notificationService.createNotification(request);

        assertNotNull(result);
        assertEquals(notifId, result.getId());
        verify(notificationLogRepository).save(any(NotificationLog.class));
    }

    @Test
    @DisplayName("Should process async event idempotently")
    void processNotificationEvent_Idempotent() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(eventId)
                .recipientId(recipientId)
                .eventType("JOB_APPLIED")
                .title("Application Received")
                .message("Application received for Google")
                .type("EMAIL")
                .build();

        when(notificationLogRepository.existsByEventId(eventId)).thenReturn(true);

        notificationService.processNotificationEvent(event);

        verify(notificationLogRepository, never()).save(any(NotificationLog.class));
    }
}
