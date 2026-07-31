package com.careeros.notification.service.impl;

import com.careeros.notification.dto.event.NotificationEvent;
import com.careeros.notification.dto.request.NotificationCreateRequest;
import com.careeros.notification.dto.response.NotificationResponse;
import com.careeros.notification.dto.response.PageResponse;
import com.careeros.notification.entity.NotificationLog;
import com.careeros.notification.enums.NotificationStatus;
import com.careeros.notification.enums.NotificationType;
import com.careeros.notification.exception.NotificationServiceException;
import com.careeros.notification.mapper.NotificationMapper;
import com.careeros.notification.repository.NotificationLogRepository;
import com.careeros.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationLogRepository notificationLogRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationLogRepository notificationLogRepository, NotificationMapper notificationMapper) {
        this.notificationLogRepository = notificationLogRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        log.info("Creating notification for recipient ID: {}", request.getRecipientId());

        NotificationLog entity = NotificationLog.builder()
                .recipientId(request.getRecipientId())
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType() != null ? request.getType() : NotificationType.IN_APP)
                .status(NotificationStatus.SENT)
                .eventId(request.getEventId())
                .build();

        NotificationLog saved = notificationLogRepository.save(entity);
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getNotificationsByRecipient(UUID recipientId, int page, int size) {
        log.info("Retrieving notifications for recipient ID: {}, page: {}, size: {}", recipientId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationLog> logs = notificationLogRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId, pageable);
        return PageResponse.fromPage(logs.map(notificationMapper::toResponse));
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(UUID id) {
        log.info("Marking notification ID {} as READ", id);

        NotificationLog entity = notificationLogRepository.findById(id)
                .orElseThrow(() -> new NotificationServiceException("Notification not found with ID: " + id));

        entity.setStatus(NotificationStatus.READ);
        NotificationLog updated = notificationLogRepository.save(entity);
        return notificationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void processNotificationEvent(NotificationEvent event) {
        log.info("Processing async notification event ID: {}, type: {}", event.getEventId(), event.getEventType());

        if (event.getEventId() != null && notificationLogRepository.existsByEventId(event.getEventId())) {
            log.warn("Duplicate notification event received, skipping event ID: {}", event.getEventId());
            return;
        }

        NotificationType type;
        try {
            type = NotificationType.valueOf(event.getType());
        } catch (Exception e) {
            type = NotificationType.IN_APP;
        }

        NotificationLog entity = NotificationLog.builder()
                .recipientId(event.getRecipientId())
                .title(event.getTitle())
                .message(event.getMessage())
                .type(type)
                .status(NotificationStatus.SENT)
                .eventId(event.getEventId())
                .build();

        notificationLogRepository.save(entity);
        log.info("Async notification log saved for recipient: {}", event.getRecipientId());
    }
}
