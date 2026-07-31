package com.careeros.audit.consumer;

import com.careeros.audit.dto.event.AuditEvent;
import com.careeros.audit.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AuditKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditKafkaConsumer.class);

    private final AuditLogService auditLogService;

    public AuditKafkaConsumer(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @KafkaListener(
            topics = {
                    "careeros.audit.events",
                    "careeros.auth.events",
                    "careeros.profile.events",
                    "careeros.ai.events",
                    "careeros.job.events"
            },
            groupId = "${spring.kafka.consumer.group-id:careeros-audit-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAuditEvent(@Payload AuditEvent event) {
        log.info("Kafka Consumer received event ID {} for service {}", 
                event.getEventId(), event.getServiceName());
        auditLogService.processKafkaEvent(event);
    }
}
