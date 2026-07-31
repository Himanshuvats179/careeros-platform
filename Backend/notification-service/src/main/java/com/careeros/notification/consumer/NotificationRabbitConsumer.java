package com.careeros.notification.consumer;

import com.careeros.notification.config.RabbitMQConfig;
import com.careeros.notification.dto.event.NotificationEvent;
import com.careeros.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationRabbitConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationRabbitConsumer.class);

    private final NotificationService notificationService;

    public NotificationRabbitConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeNotificationMessage(NotificationEvent event) {
        log.info("Received RabbitMQ notification event ID: {}", event.getEventId());
        notificationService.processNotificationEvent(event);
    }
}
