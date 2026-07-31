# Notification Service - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Consumes event notifications from RabbitMQ queues (`careeros.notification.queue`) and Apache Kafka topics (`careeros.notification.events`).
2. Dispatches multi-channel alerts (Email, SMS, In-App Notifications, Mobile Push).
3. Enforces event processing idempotency (`existsByEventId`) to avoid duplicate user messages.
4. Persists notification logs and read/unread status flags in PostgreSQL.
5. Exposes REST APIs for candidates to fetch unread alerts and mark notifications as READ.

## 2. Why This Service Exists
The Notification Service decouples candidate communications from core transaction services. When a job application status transitions or a profile is updated, business services publish async messages, allowing Notification Service to format and deliver templates independently without blocking HTTP request threads.

## 3. Microservice Dependencies & Interactions
- **API Gateway (`:8080`)**: Routes client requests (`/api/v1/notifications`).
- **Service Registry (`:8761`)**: Eureka instance registration.
- **RabbitMQ (`:5672`)**: Direct exchange `careeros.notification.exchange` and durable queue `careeros.notification.queue`.
- **Apache Kafka (`:9092`)**: Consumer group `careeros-notification-group` reading event streams.
- **PostgreSQL (`:5432`)**: Stores `notification_logs` table.

## 4. Package & Folder Structure Explanation

### `com.careeros.notification.controller`
- **Why Controller exists**: Exposes REST endpoints (`/api/v1/notifications`, `/api/v1/notifications/recipient/{recipientId}`, `/api/v1/notifications/{id}/read`).

### `com.careeros.notification.consumer`
- **Why Consumer exists**: Encapsulates `@RabbitListener` (`NotificationRabbitConsumer`) and `@KafkaListener` (`NotificationKafkaConsumer`) message handlers.

### `com.careeros.notification.service` & `impl`
- **Why Service exists**: Handles idempotency validation, notification log persistence, and status updates.

### `com.careeros.notification.repository`
- **Why Repository exists**: Provides queries for retrieving candidate notifications sorted by creation timestamp.

### `com.careeros.notification.entity`
- **Why Entity exists**: Maps `NotificationLog` table with soft delete (`is_deleted`) and optimistic locking (`@Version`).

## 5. End-to-End Request Flow
1. Job Service or Auth Service publishes `NotificationEvent` payload to RabbitMQ exchange or Kafka topic.
2. `NotificationRabbitConsumer` or `NotificationKafkaConsumer` receives payload.
3. `NotificationServiceImpl.processNotificationEvent` verifies idempotency via `existsByEventId`.
4. Message formatted for target channel (In-App / Email / SMS) and saved to PostgreSQL `notification_logs`.
5. Candidate retrieves alerts via `GET /api/v1/notifications/recipient/{recipientId}` on React 19 Frontend.

## 6. Senior Interview Questions & Production Patterns
- **Q: How do you guarantee at-least-once notification delivery without spamming users?**
  - *Answer*: Producers generate a unique `eventId` UUID for every event. Consumers execute `existsByEventId(eventId)` before processing, silently dropping duplicate retries.

## 7. Future AI / ML / NLP & AWS Expansion Scope
- **AWS SES & SNS Integration**: Route email templates directly through AWS Simple Email Service (SES) and SMS via AWS Simple Notification Service (SNS).
- **Optimal Notification Time ML Model**: Predict optimal candidate engagement times using AWS SageMaker to delay non-urgent push alerts until high-open windows.
