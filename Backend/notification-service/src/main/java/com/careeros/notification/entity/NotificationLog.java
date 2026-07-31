package com.careeros.notification.entity;

import com.careeros.notification.enums.NotificationStatus;
import com.careeros.notification.enums.NotificationType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "notification_logs", indexes = {
        @Index(name = "idx_notif_recipient", columnList = "recipient_id"),
        @Index(name = "idx_notif_status", columnList = "status"),
        @Index(name = "idx_notif_type", columnList = "type")
})
@SQLDelete(sql = "UPDATE notification_logs SET is_deleted = true WHERE id = ? AND version = ?")
@Where(clause = "is_deleted = false")
public class NotificationLog extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type = NotificationType.IN_APP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "event_id")
    private UUID eventId;

    public NotificationLog() {}

    public NotificationLog(UUID id, UUID recipientId, String title, String message, NotificationType type, NotificationStatus status, UUID eventId) {
        this.id = id;
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.status = status;
        this.eventId = eventId;
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

    public static NotificationLogBuilder builder() { return new NotificationLogBuilder(); }

    public static class NotificationLogBuilder {
        private UUID id;
        private UUID recipientId;
        private String title;
        private String message;
        private NotificationType type = NotificationType.IN_APP;
        private NotificationStatus status = NotificationStatus.PENDING;
        private UUID eventId;

        public NotificationLogBuilder id(UUID id) { this.id = id; return this; }
        public NotificationLogBuilder recipientId(UUID recipientId) { this.recipientId = recipientId; return this; }
        public NotificationLogBuilder title(String title) { this.title = title; return this; }
        public NotificationLogBuilder message(String message) { this.message = message; return this; }
        public NotificationLogBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationLogBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationLogBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }

        public NotificationLog build() {
            return new NotificationLog(id, recipientId, title, message, type, status, eventId);
        }
    }
}
