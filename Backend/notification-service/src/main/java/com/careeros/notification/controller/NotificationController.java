package com.careeros.notification.controller;

import com.careeros.common.dto.ApiResponse;
import com.careeros.notification.dto.request.NotificationCreateRequest;
import com.careeros.notification.dto.response.NotificationResponse;
import com.careeros.notification.dto.response.PageResponse;
import com.careeros.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "APIs for sending alerts, retrieving candidate notifications, and updating read statuses.")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @Operation(summary = "Send Notification", description = "Creates and dispatches a multi-channel notification (Email, SMS, In-App).")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(@Valid @RequestBody NotificationCreateRequest request) {
        log.info("REST request to send notification to recipient: {}", request.getRecipientId());
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification dispatched successfully", response));
    }

    @GetMapping("/recipient/{recipientId}")
    @Operation(summary = "Get Recipient Notifications", description = "Retrieves paginated notification history for a candidate or user.")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotificationsByRecipient(
            @PathVariable UUID recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST request to get notifications for recipient ID: {}", recipientId);
        PageResponse<NotificationResponse> response = notificationService.getNotificationsByRecipient(recipientId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Recipient notifications retrieved successfully", response));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark Notification as Read", description = "Updates notification status from SENT to READ.")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable UUID id) {
        log.info("REST request to mark notification ID {} as READ", id);
        NotificationResponse response = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read successfully", response));
    }
}
