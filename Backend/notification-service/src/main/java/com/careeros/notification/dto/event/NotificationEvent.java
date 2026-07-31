package com.careeros.notification.dto.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationEvent {

    private UUID eventId;
    private UUID recipientId;
    private String eventType;
    private String title;
    private String message;
    private String type;
    private LocalDateTime timestamp;

    public NotificationEvent() {}

    public NotificationEvent(UUID eventId, UUID recipientId, String eventType, String title, String message, String type, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.recipientId = recipientId;
        this.eventType = eventType;
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public UUID getRecipientId() { return recipientId; }
    public void setRecipientId(UUID recipientId) { this.recipientId = recipientId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static NotificationEventBuilder builder() { return new NotificationEventBuilder(); }

    public static class NotificationEventBuilder {
        private UUID eventId = UUID.randomUUID();
        private UUID recipientId;
        private String eventType;
        private String title;
        private String message;
        private String type = "IN_APP";
        private LocalDateTime timestamp = LocalDateTime.now();

        public NotificationEventBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public NotificationEventBuilder recipientId(UUID recipientId) { this.recipientId = recipientId; return this; }
        public NotificationEventBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public NotificationEventBuilder title(String title) { this.title = title; return this; }
        public NotificationEventBuilder message(String message) { this.message = message; return this; }
        public NotificationEventBuilder type(String type) { this.type = type; return this; }
        public NotificationEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public NotificationEvent build() {
            return new NotificationEvent(eventId, recipientId, eventType, title, message, type, timestamp);
        }
    }
}
