package com.careeros.auth.dto.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuthEvent {

    private UUID eventId;
    private String correlationId;
    private UUID userId;
    private String eventType;
    private String serviceName;
    private String action;
    private String requestData;
    private String responseData;
    private String status;
    private LocalDateTime timestamp;

    public AuthEvent() {}

    public AuthEvent(UUID eventId, String correlationId, UUID userId, String eventType, String serviceName, String action, String requestData, String responseData, String status, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.correlationId = correlationId;
        this.userId = userId;
        this.eventType = eventType;
        this.serviceName = serviceName;
        this.action = action;
        this.requestData = requestData;
        this.responseData = responseData;
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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static AuthEventBuilder builder() { return new AuthEventBuilder(); }

    public static class AuthEventBuilder {
        private UUID eventId = UUID.randomUUID();
        private String correlationId;
        private UUID userId;
        private String eventType;
        private String serviceName = "AUTH_SERVICE";
        private String action;
        private String requestData;
        private String responseData;
        private String status = "SUCCESS";
        private LocalDateTime timestamp = LocalDateTime.now();

        public AuthEventBuilder eventId(UUID eventId) { this.eventId = eventId; return this; }
        public AuthEventBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public AuthEventBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AuthEventBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public AuthEventBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public AuthEventBuilder action(String action) { this.action = action; return this; }
        public AuthEventBuilder requestData(String requestData) { this.requestData = requestData; return this; }
        public AuthEventBuilder responseData(String responseData) { this.responseData = responseData; return this; }
        public AuthEventBuilder status(String status) { this.status = status; return this; }
        public AuthEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public AuthEvent build() {
            return new AuthEvent(eventId, correlationId, userId, eventType, serviceName, action, requestData, responseData, status, timestamp);
        }
    }
}
