package com.careeros.audit.controller;

import com.careeros.audit.dto.request.AuditCreateRequest;
import com.careeros.audit.dto.request.AuditSearchCriteria;
import com.careeros.audit.dto.response.AuditLogResponse;
import com.careeros.audit.dto.response.PageResponse;
import com.careeros.audit.service.AuditLogService;
import com.careeros.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@Tag(name = "Audit Log Management", description = "APIs for querying enterprise audit logs, filtering by service/user, and date range queries.")
public class AuditLogController {

    private static final Logger log = LoggerFactory.getLogger(AuditLogController.class);

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    @Operation(summary = "Create Audit Log", description = "Manually ingests an audit log record into the system.")
    public ResponseEntity<ApiResponse<AuditLogResponse>> createAuditLog(@Valid @RequestBody AuditCreateRequest request) {
        log.info("REST request to create audit log eventId: {}", request.getEventId());
        AuditLogResponse response = auditLogService.createAuditLog(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Audit log created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Audit Log by ID", description = "Retrieves an audit log record by its UUID primary key.")
    public ResponseEntity<ApiResponse<AuditLogResponse>> getAuditLogById(@PathVariable UUID id) {
        log.info("REST request to get audit log ID: {}", id);
        AuditLogResponse response = auditLogService.getAuditLogById(id);
        return ResponseEntity.ok(ApiResponse.success("Audit log retrieved successfully", response));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Audit Logs for User", description = "Retrieves paginated audit logs for a specific user ID.")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> getAuditLogsByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST request to get audit logs for user ID: {}", userId);
        PageResponse<AuditLogResponse> response = auditLogService.getAuditLogsByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success("User audit logs retrieved successfully", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search & Filter Audit Logs", description = "Search audit logs with filters for userId, serviceName, eventType, status, date range (startDate/endDate), pagination, and sorting.")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogResponse>>> searchAuditLogs(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
                .userId(userId)
                .serviceName(serviceName)
                .eventType(eventType)
                .status(status)
                .search(search)
                .startDate(startDate)
                .endDate(endDate)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PageResponse<AuditLogResponse> response = auditLogService.searchAuditLogs(criteria);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }
}
