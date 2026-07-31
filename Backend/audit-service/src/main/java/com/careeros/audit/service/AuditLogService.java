package com.careeros.audit.service;

import com.careeros.audit.dto.event.AuditEvent;
import com.careeros.audit.dto.request.AuditCreateRequest;
import com.careeros.audit.dto.request.AuditSearchCriteria;
import com.careeros.audit.dto.response.AuditLogResponse;
import com.careeros.audit.dto.response.PageResponse;

import java.util.UUID;

public interface AuditLogService {
    void processKafkaEvent(AuditEvent event);
    AuditLogResponse createAuditLog(AuditCreateRequest request);
    AuditLogResponse getAuditLogById(UUID id);
    PageResponse<AuditLogResponse> getAuditLogsByUserId(UUID userId, int page, int size);
    PageResponse<AuditLogResponse> searchAuditLogs(AuditSearchCriteria criteria);
}
