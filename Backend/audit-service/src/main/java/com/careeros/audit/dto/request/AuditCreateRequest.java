package com.careeros.audit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AuditCreateRequest {

    @NotNull(message = "Event ID is required")
    private UUID eventId;

    private String correlationId;
    private UUID userId;

    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Action description is required")
    private String action;

    private String requestData;
    private String responseData;
    private String ipAddress;
    private String device;
    private String browser;
    private String status;

    public AuditCreateRequest() {}

    public AuditCreateRequest(UUID eventId, String correlationId, UUID userId, String eventType, String serviceName, String action, String requestData, String responseData, String ipAddress, String device, String browser, String status) {
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

    public static AuditCreateRequestBuilder builder() { return new AuditCreateRequestBuilder(); }

    public static class AuditCreateRequestBuilder {
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

        public AuditCreateRequestBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public AuditCreateRequestBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public AuditCreateRequestBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AuditCreateRequestBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public AuditCreateRequestBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public AuditCreateRequestBuilder action(String action) { this.action = action; return this; }
        public AuditCreateRequestBuilder requestData(String requestData) { this.requestData = requestData; return this; }
        public AuditCreateRequestBuilder responseData(String responseData) { this.responseData = responseData; return this; }
        public AuditCreateRequestBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditCreateRequestBuilder device(String device) { this.device = device; return this; }
        public AuditCreateRequestBuilder browser(String browser) { this.browser = browser; return this; }
        public AuditCreateRequestBuilder status(String status) { this.status = status; return this; }

        public AuditCreateRequest build() {
            return new AuditCreateRequest(eventId, correlationId, userId, eventType, serviceName, action, requestData, responseData, ipAddress, device, browser, status);
        }
    }
}
