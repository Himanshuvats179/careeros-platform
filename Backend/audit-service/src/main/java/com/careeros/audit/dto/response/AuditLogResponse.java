package com.careeros.audit.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLogResponse {

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
    private String status;
    private LocalDateTime timestamp;
    private boolean mlFeatureExported;
    private boolean awsCloudWatchExported;

    public AuditLogResponse() {}

    public AuditLogResponse(UUID id, UUID eventId, String correlationId, UUID userId, String eventType, String serviceName, String action, String requestData, String responseData, String ipAddress, String device, String browser, String status, LocalDateTime timestamp, boolean mlFeatureExported, boolean awsCloudWatchExported) {
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

    public static AuditLogResponseBuilder builder() { return new AuditLogResponseBuilder(); }

    public static class AuditLogResponseBuilder {
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
        private String status;
        private LocalDateTime timestamp;
        private boolean mlFeatureExported;
        private boolean awsCloudWatchExported;

        public AuditLogResponseBuilder id(UUID id) { this.id = id; return this; }
        public AuditLogResponseBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public AuditLogResponseBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public AuditLogResponseBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AuditLogResponseBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public AuditLogResponseBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public AuditLogResponseBuilder action(String action) { this.action = action; return this; }
        public AuditLogResponseBuilder requestData(String requestData) { this.requestData = requestData; return this; }
        public AuditLogResponseBuilder responseData(String responseData) { this.responseData = responseData; return this; }
        public AuditLogResponseBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditLogResponseBuilder device(String device) { this.device = device; return this; }
        public AuditLogResponseBuilder browser(String browser) { this.browser = browser; return this; }
        public AuditLogResponseBuilder status(String status) { this.status = status; return this; }
        public AuditLogResponseBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditLogResponseBuilder mlFeatureExported(boolean mlFeatureExported) { this.mlFeatureExported = mlFeatureExported; return this; }
        public AuditLogResponseBuilder awsCloudWatchExported(boolean awsCloudWatchExported) { this.awsCloudWatchExported = awsCloudWatchExported; return this; }

        public AuditLogResponse build() {
            return new AuditLogResponse(id, eventId, correlationId, userId, eventType, serviceName, action, requestData, responseData, ipAddress, device, browser, status, timestamp, mlFeatureExported, awsCloudWatchExported);
        }
    }
}
