# Audit Service - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Consumes centralized event streams from all platform microservices over Apache Kafka.
2. Implements idempotent processing to prevent duplicate audit logs during Kafka network retries.
3. Provides retry logic and Dead Letter Queue (DLQ) recovery for unrecoverable event errors.
4. Persists structured audit trail records into PostgreSQL database tables.
5. Exposes REST APIs for date-range searching, user audit history, and service-based filtering.

## 2. Why This Service Exists
The Audit Service provides enterprise compliance, security tracking, and operational visibility across CareerOS. Every critical platform action (`USER_REGISTERED`, `RESUME_UPLOADED`, `AI_RECOMMENDATION_GENERATED`, `JOB_APPLIED`) is captured asynchronously without blocking primary user request flows.

## 3. Microservice Dependencies & Interactions
- **Apache Kafka (`:9092`)**: Consumes from topics `careeros.audit.events`, `careeros.auth.events`, `careeros.profile.events`, `careeros.ai.events`, `careeros.job.events`.
- **PostgreSQL (`:5432`)**: Stores `audit_logs` records.
- **Eureka Service Registry (`:8761`)**: Service registration for API Gateway routing.
- **API Gateway (`:8080`)**: Routes admin and user REST search queries (`/api/v1/audit-logs`) to Audit Service.

## 4. Package & Folder Structure Explanation

### `com.careeros.audit.consumer`
- **Why Consumer exists**: Hosts `@KafkaListener` annotations to consume microservice events from Kafka topics asynchronously.
- **Production Pattern**: Configured with `ConcurrentKafkaListenerContainerFactory` for multi-threaded parallel event processing.

### `com.careeros.audit.config`
- **Why Config exists**: Configures `KafkaConsumerConfig` (JsonDeserializer, DefaultErrorHandler, DeadLetterPublishingRecoverer, Exponential Backoff) and Swagger OpenAPI specs.

### `com.careeros.audit.controller`
- **Why Controller exists**: Exposes REST endpoints for searching audit logs by date range (`startDate`/`endDate`), `userId`, `serviceName`, `eventType`, and pagination parameters.

### `com.careeros.audit.service` & `impl`
- **Why Service exists**: Contains business logic, idempotency checks (`existsByEventId`), manual audit creation, and JPQL search query execution.

### `com.careeros.audit.repository`
- **Why Repository exists**: Provides custom JPQL search specifications and indexed lookups for $O(1)$ idempotency verification.

### `com.careeros.audit.dto`
- **Why DTO exists**: Defines `AuditEvent` (Kafka payload format), `AuditSearchCriteria`, `AuditCreateRequest`, `AuditLogResponse`, and generic `PageResponse`.

### `com.careeros.audit.entity`
- **Why Entity exists**: Maps database table `audit_logs` to Java object model. Contains extensions for `ml_feature_exported` and `aws_cloudwatch_exported`.

## 5. End-to-End Request & Event Flow
1. User uploads a resume in `profile-service`.
2. `profile-service` publishes `AuditEvent` to Kafka topic `careeros.profile.events`.
3. `AuditKafkaConsumer` receives message and passes to `AuditLogService.processKafkaEvent`.
4. `AuditLogServiceImpl` queries `AuditLogRepository.existsByEventId(eventId)`.
5. If `eventId` exists (duplicate Kafka message), consumption is safely skipped.
6. If new, `AuditLog` entity is saved to PostgreSQL `audit_logs` table.
7. If database fails 3 times, `DeadLetterPublishingRecoverer` routes event to `careeros.profile.events.DLQ`.

## 6. Senior Interview Questions & Production Patterns
- **Q: How do you guarantee idempotency in an at-least-once Kafka consumer?**
  - *Answer*: We assign a unique `eventId` UUID at event creation. Before saving, `AuditLogServiceImpl` checks database index `idx_audit_event_id`. If present, the duplicate delivery is acknowledged without double-writing.
- **Q: What happens to poisonous messages that cannot be processed?**
  - *Answer*: They are handled by `DefaultErrorHandler` with a 3-retry fixed backoff strategy before being forwarded to a Dead Letter Queue (`.DLQ`) topic for asynchronous inspection.

## 7. Future AI / ML / NLP & AWS Expansion Scope
- **User Behavior ML Pipeline**: Audit events track candidate interactions (`JOB_APPLIED`, `SKILL_ADDED`). The `ml_feature_exported` flag allows Spark/Airflow pipelines to extract user preference features for candidate-job matching ML models.
- **AWS MSK & CloudWatch Integration**: Direct event export to AWS MSK and AWS CloudWatch Logs for enterprise security analytics.
