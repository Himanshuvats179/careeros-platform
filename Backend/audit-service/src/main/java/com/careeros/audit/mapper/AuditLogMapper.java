package com.careeros.audit.mapper;

import com.careeros.audit.dto.event.AuditEvent;
import com.careeros.audit.dto.request.AuditCreateRequest;
import com.careeros.audit.dto.response.AuditLogResponse;
import com.careeros.audit.entity.AuditLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditLogMapper {

    public AuditLog toEntity(AuditEvent event) {
        if (event == null) return null;

        return AuditLog.builder()
                .eventId(event.getEventId())
                .correlationId(event.getCorrelationId())
                .userId(event.getUserId())
                .eventType(event.getEventType())
                .serviceName(event.getServiceName())
                .action(event.getAction())
                .requestData(event.getRequestData())
                .responseData(event.getResponseData())
                .ipAddress(event.getIpAddress())
                .device(event.getDevice())
                .browser(event.getBrowser())
                .status(event.getStatus() != null ? event.getStatus() : "SUCCESS")
                .timestamp(event.getTimestamp() != null ? event.getTimestamp() : LocalDateTime.now())
                .build();
    }

    public AuditLog toEntity(AuditCreateRequest req) {
        if (req == null) return null;

        return AuditLog.builder()
                .eventId(req.getEventId())
                .correlationId(req.getCorrelationId())
                .userId(req.getUserId())
                .eventType(req.getEventType())
                .serviceName(req.getServiceName())
                .action(req.getAction())
                .requestData(req.getRequestData())
                .responseData(req.getResponseData())
                .ipAddress(req.getIpAddress())
                .device(req.getDevice())
                .browser(req.getBrowser())
                .status(req.getStatus() != null ? req.getStatus() : "SUCCESS")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public AuditLogResponse toResponse(AuditLog entity) {
        if (entity == null) return null;

        return AuditLogResponse.builder()
                .id(entity.getId())
                .eventId(entity.getEventId())
                .correlationId(entity.getCorrelationId())
                .userId(entity.getUserId())
                .eventType(entity.getEventType())
                .serviceName(entity.getServiceName())
                .action(entity.getAction())
                .requestData(entity.getRequestData())
                .responseData(entity.getResponseData())
                .ipAddress(entity.getIpAddress())
                .device(entity.getDevice())
                .browser(entity.getBrowser())
                .status(entity.getStatus())
                .timestamp(entity.getTimestamp())
                .mlFeatureExported(entity.isMlFeatureExported())
                .awsCloudWatchExported(entity.isAwsCloudWatchExported())
                .build();
    }
}
