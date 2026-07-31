package com.careeros.audit.service.impl;

import com.careeros.audit.dto.event.AuditEvent;
import com.careeros.audit.dto.request.AuditCreateRequest;
import com.careeros.audit.dto.request.AuditSearchCriteria;
import com.careeros.audit.dto.response.AuditLogResponse;
import com.careeros.audit.dto.response.PageResponse;
import com.careeros.audit.entity.AuditLog;
import com.careeros.audit.exception.AuditServiceException;
import com.careeros.audit.mapper.AuditLogMapper;
import com.careeros.audit.repository.AuditLogRepository;
import com.careeros.audit.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    @Transactional
    public void processKafkaEvent(AuditEvent event) {
        if (event == null || event.getEventId() == null) {
            log.warn("Received null or invalid Kafka AuditEvent");
            return;
        }

        // Idempotency Check: Prevent duplicate event logging on Kafka retries
        if (auditLogRepository.existsByEventId(event.getEventId())) {
            log.info("Idempotency match: Audit log event ID {} already processed. Skipping duplicate record.", event.getEventId());
            return;
        }

        log.info("Processing Kafka AuditEvent: type={}, service={}, userId={}", 
                event.getEventType(), event.getServiceName(), event.getUserId());

        AuditLog auditLog = auditLogMapper.toEntity(event);
        auditLogRepository.save(auditLog);
        log.info("Audit log successfully saved to database with ID: {}", auditLog.getId());
    }

    @Override
    @Transactional
    public AuditLogResponse createAuditLog(AuditCreateRequest request) {
        log.info("Creating manual audit log for eventType: {}", request.getEventType());
        if (auditLogRepository.existsByEventId(request.getEventId())) {
            throw new AuditServiceException("Audit log with Event ID " + request.getEventId() + " already exists.");
        }

        AuditLog auditLog = auditLogMapper.toEntity(request);
        AuditLog savedLog = auditLogRepository.save(auditLog);
        return auditLogMapper.toResponse(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLogById(UUID id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditServiceException("Audit log not found with ID: " + id));
        return auditLogMapper.toResponse(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> getAuditLogsByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLog> pageResult = auditLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
        Page<AuditLogResponse> responsePage = pageResult.map(auditLogMapper::toResponse);
        return PageResponse.fromPage(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> searchAuditLogs(AuditSearchCriteria criteria) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(criteria.getSortDirection()),
                criteria.getSortBy()
        );
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Page<AuditLog> pageResult = auditLogRepository.searchAuditLogs(
                criteria.getUserId(),
                criteria.getServiceName(),
                criteria.getEventType(),
                criteria.getStatus(),
                criteria.getStartDate(),
                criteria.getEndDate(),
                criteria.getSearch(),
                pageable
        );

        Page<AuditLogResponse> responsePage = pageResult.map(auditLogMapper::toResponse);
        return PageResponse.fromPage(responsePage);
    }
}
