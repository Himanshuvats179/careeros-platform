package com.careeros.audit.repository;

import com.careeros.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID>, JpaSpecificationExecutor<AuditLog> {

    Optional<AuditLog> findByEventId(UUID eventId);

    boolean existsByEventId(UUID eventId);

    Page<AuditLog> findByUserIdOrderByTimestampDesc(UUID userId, Pageable pageable);

    @Query("""
        SELECT a FROM AuditLog a 
        WHERE (:userId IS NULL OR a.userId = :userId)
          AND (:serviceName IS NULL OR LOWER(a.serviceName) = LOWER(:serviceName))
          AND (:eventType IS NULL OR LOWER(a.eventType) = LOWER(:eventType))
          AND (:status IS NULL OR LOWER(a.status) = LOWER(:status))
          AND (:startDate IS NULL OR a.timestamp >= :startDate)
          AND (:endDate IS NULL OR a.timestamp <= :endDate)
          AND (:search IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(a.requestData) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<AuditLog> searchAuditLogs(
            @Param("userId") UUID userId,
            @Param("serviceName") String serviceName,
            @Param("eventType") String eventType,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("search") String search,
            Pageable pageable
    );
}
