package com.careeros.notification.repository;

import com.careeros.notification.entity.NotificationLog;
import com.careeros.notification.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
    Page<NotificationLog> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);
    Page<NotificationLog> findByRecipientIdAndStatus(UUID recipientId, NotificationStatus status, Pageable pageable);
    boolean existsByEventId(UUID eventId);
}
