package com.careeros.notification.mapper;

import com.careeros.notification.dto.response.NotificationResponse;
import com.careeros.notification.entity.NotificationLog;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(NotificationLog entity) {
        if (entity == null) return null;

        return NotificationResponse.builder()
                .id(entity.getId())
                .recipientId(entity.getRecipientId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .type(entity.getType())
                .status(entity.getStatus())
                .eventId(entity.getEventId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
