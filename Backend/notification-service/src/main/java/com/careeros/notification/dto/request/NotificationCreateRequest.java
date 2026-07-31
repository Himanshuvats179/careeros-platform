package com.careeros.notification.dto.request;

import com.careeros.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class NotificationCreateRequest {

    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    private NotificationType type = NotificationType.IN_APP;
    private UUID eventId;

    public NotificationCreateRequest() {}

    public NotificationCreateRequest(UUID recipientId, String title, String message, NotificationType type, UUID eventId) {
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
        if (type != null) this.type = type;
        this.eventId = eventId;
    }

    public UUID getRecipientId() { return recipientId; }
    public void setRecipientId(UUID recipientId) { this.recipientId = recipientId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public static NotificationCreateRequestBuilder builder() { return new NotificationCreateRequestBuilder(); }

    public static class NotificationCreateRequestBuilder {
        private UUID recipientId;
        private String title;
        private String message;
        private NotificationType type = NotificationType.IN_APP;
        private UUID eventId;

        public NotificationCreateRequestBuilder recipientId(UUID recipientId) { this.recipientId = recipientId; return this; }
        public NotificationCreateRequestBuilder title(String title) { this.title = title; return this; }
        public NotificationCreateRequestBuilder message(String message) { this.message = message; return this; }
        public NotificationCreateRequestBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationCreateRequestBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }

        public NotificationCreateRequest build() {
            return new NotificationCreateRequest(recipientId, title, message, type, eventId);
        }
    }
}
