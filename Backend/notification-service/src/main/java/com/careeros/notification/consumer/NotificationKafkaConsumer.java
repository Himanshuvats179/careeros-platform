package com.careeros.notification.consumer;

import com.careeros.notification.dto.event.NotificationEvent;
import com.careeros.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationKafkaConsumer.class);

    private final NotificationService notificationService;

    public NotificationKafkaConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "careeros.notification.events", groupId = "${spring.kafka.consumer.group-id:careeros-notification-group}")
    public void consumeKafkaNotificationEvent(NotificationEvent event) {
        log.info("Received Kafka notification event ID: {}", event.getEventId());
        notificationService.processNotificationEvent(event);
    }
}
