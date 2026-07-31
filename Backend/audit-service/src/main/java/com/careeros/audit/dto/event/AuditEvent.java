package com.careeros.audit.dto.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditEvent {

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

    public AuditEvent() {}

    public AuditEvent(UUID eventId, String correlationId, UUID userId, String eventType, String serviceName, String action, String requestData, String responseData, String ipAddress, String device, String browser, String status, LocalDateTime timestamp) {
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
    }

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

    public static AuditEventBuilder builder() { return new AuditEventBuilder(); }

    public static class AuditEventBuilder {
        private UUID eventId = UUID.randomUUID();
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

        public AuditEventBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public AuditEventBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public AuditEventBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AuditEventBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public AuditEventBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public AuditEventBuilder action(String action) { this.action = action; return this; }
        public AuditEventBuilder requestData(String requestData) { this.requestData = requestData; return this; }
        public AuditEventBuilder responseData(String responseData) { this.responseData = responseData; return this; }
        public AuditEventBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditEventBuilder device(String device) { this.device = device; return this; }
        public AuditEventBuilder browser(String browser) { this.browser = browser; return this; }
        public AuditEventBuilder status(String status) { this.status = status; return this; }
        public AuditEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public AuditEvent build() {
            return new AuditEvent(eventId, correlationId, userId, eventType, serviceName, action, requestData, responseData, ipAddress, device, browser, status, timestamp);
        }
    }
}
