package com.careeros.notification.dto.response;

import com.careeros.notification.enums.NotificationStatus;
import com.careeros.notification.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationResponse {

    private UUID id;
    private UUID recipientId;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private UUID eventId;
    private LocalDateTime createdAt;

    public NotificationResponse() {}

    public NotificationResponse(UUID id, UUID recipientId, String title, String message, NotificationType type, NotificationStatus status, UUID eventId, LocalDateTime createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.status = status;
        this.eventId = eventId;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getRecipientId() { return recipientId; }
    public void setRecipientId(UUID recipientId) { this.recipientId = recipientId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static NotificationResponseBuilder builder() { return new NotificationResponseBuilder(); }

    public static class NotificationResponseBuilder {
        private UUID id;
        private UUID recipientId;
        private String title;
        private String message;
        private NotificationType type;
        private NotificationStatus status;
        private UUID eventId;
        private LocalDateTime createdAt;

        public NotificationResponseBuilder id(UUID id) { this.id = id; return this; }
        public NotificationResponseBuilder recipientId(UUID recipientId) { this.recipientId = recipientId; return this; }
        public NotificationResponseBuilder title(String title) { this.title = title; return this; }
        public NotificationResponseBuilder message(String message) { this.message = message; return this; }
        public NotificationResponseBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationResponseBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationResponseBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public NotificationResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public NotificationResponse build() {
            return new NotificationResponse(id, recipientId, title, message, type, status, eventId, createdAt);
        }
    }
}
