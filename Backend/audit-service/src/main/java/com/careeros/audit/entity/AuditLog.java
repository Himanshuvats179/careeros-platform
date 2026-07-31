package com.careeros.audit.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_event_id", columnList = "event_id", unique = true),
                @Index(name = "idx_audit_user_id_timestamp", columnList = "user_id, timestamp"),
                @Index(name = "idx_audit_service_event", columnList = "service_name, event_type"),
                @Index(name = "idx_audit_timestamp", columnList = "timestamp")
        }
)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(nullable = false, length = 150)
    private String action;

    @Column(name = "request_data", columnDefinition = "TEXT")
    private String requestData;

    @Column(name = "response_data", columnDefinition = "TEXT")
    private String responseData;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(length = 100)
    private String device;

    @Column(length = 100)
    private String browser;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // ML Feature Extraction & AWS Export Scope
    @Column(name = "ml_feature_exported", nullable = false)
    private boolean mlFeatureExported = false;

    @Column(name = "aws_cloudwatch_exported", nullable = false)
    private boolean awsCloudWatchExported = false;

    public AuditLog() {}

    public AuditLog(UUID id, UUID eventId, String correlationId, UUID userId, String eventType, String serviceName, String action, String requestData, String responseData, String ipAddress, String device, String browser, String status, LocalDateTime timestamp, boolean mlFeatureExported, boolean awsCloudWatchExported) {
        this.id = id;
        this.eventId = eventId;
        this.correlationId = correlationId;
        this.userId = userId;
        this.eventType = eventType;
        this.serviceName = serviceName;
        this.action = action;
        this.requestData = requestData;
        this.responseData = responseData;
        this.ipAddress = ipAddress;
        this.device = device;
        this.browser = browser;
        this.status = status;
        this.timestamp = timestamp;
        this.mlFeatureExported = mlFeatureExported;
        this.awsCloudWatchExported = awsCloudWatchExported;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getRequestData() { return requestData; }
    public void setRequestData(String requestData) { this.requestData = requestData; }

    public String getResponseData() { return responseData; }
    public void setResponseData(String responseData) { this.responseData = responseData; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isMlFeatureExported() { return mlFeatureExported; }
    public void setMlFeatureExported(boolean mlFeatureExported) { this.mlFeatureExported = mlFeatureExported; }

    public boolean isAwsCloudWatchExported() { return awsCloudWatchExported; }
    public void setAwsCloudWatchExported(boolean awsCloudWatchExported) { this.awsCloudWatchExported = awsCloudWatchExported; }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private UUID id;
        private UUID eventId;
        private String correlationId;
        private UUID userId;
        private String eventType;
        private String serviceName;
        private String action;
        private String requestData;
        private String responseData;
        private String ipAddress;
        private String device;
        private String browser;
        private String status = "SUCCESS";
        private LocalDateTime timestamp = LocalDateTime.now();
        private boolean mlFeatureExported = false;
        private boolean awsCloudWatchExported = false;

        public AuditLogBuilder id(UUID id) { this.id = id; return this; }
        public AuditLogBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public AuditLogBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public AuditLogBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AuditLogBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public AuditLogBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public AuditLogBuilder action(String action) { this.action = action; return this; }
        public AuditLogBuilder requestData(String requestData) { this.requestData = requestData; return this; }
        public AuditLogBuilder responseData(String responseData) { this.responseData = responseData; return this; }
        public AuditLogBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditLogBuilder device(String device) { this.device = device; return this; }
        public AuditLogBuilder browser(String browser) { this.browser = browser; return this; }
        public AuditLogBuilder status(String status) { this.status = status; return this; }
        public AuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditLogBuilder mlFeatureExported(boolean mlFeatureExported) { this.mlFeatureExported = mlFeatureExported; return this; }
        public AuditLogBuilder awsCloudWatchExported(boolean awsCloudWatchExported) { this.awsCloudWatchExported = awsCloudWatchExported; return this; }

        public AuditLog build() {
            return new AuditLog(id, eventId, correlationId, userId, eventType, serviceName, action, requestData, responseData, ipAddress, device, browser, status, timestamp, mlFeatureExported, awsCloudWatchExported);
        }
    }
}
