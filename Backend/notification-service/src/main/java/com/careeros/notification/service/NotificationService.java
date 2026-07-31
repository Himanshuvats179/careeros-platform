package com.careeros.notification.service;

import com.careeros.notification.dto.event.NotificationEvent;
import com.careeros.notification.dto.request.NotificationCreateRequest;
import com.careeros.notification.dto.response.NotificationResponse;
import com.careeros.notification.dto.response.PageResponse;

import java.util.UUID;

public interface NotificationService {
    NotificationResponse createNotification(NotificationCreateRequest request);
    PageResponse<NotificationResponse> getNotificationsByRecipient(UUID recipientId, int page, int size);
    NotificationResponse markAsRead(UUID id);
    void processNotificationEvent(NotificationEvent event);
}
