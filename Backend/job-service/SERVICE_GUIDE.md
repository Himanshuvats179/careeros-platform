# Job Service - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Manages enterprise job listings, required technical skills, and salary ranges.
2. Tracks candidate job application pipelines across stages (Applied, Screened, Interview Scheduled, Offer Extended).
3. Emits `JOB_POSTED`, `JOB_APPLIED`, and `APPLICATION_STATUS_UPDATED` events to Apache Kafka.
4. Leverages Redis caching for fast candidate job search lookups.
5. Implements multi-criteria JPQL search queries over PostgreSQL database schemas.

## 2. Why This Service Exists
The Job Service decouples job postings and recruitment workflow pipelines from candidate profiles and authentication. It enables recruiters to publish roles, candidates to submit applications, and AI Agents to run skill gap and job match algorithms.

## 3. Microservice Dependencies & Interactions
- **API Gateway (`:8080`)**: Routes HTTP requests (`/api/v1/jobs`, `/api/v1/applications`) to Job Service.
- **Service Registry (`:8761`)**: Eureka dynamic instance registration.
- **Apache Kafka (`:9092`)**: Receives published events on topic `careeros.job.events` for consumption by Audit Service.
- **Redis (`:6379`)**: Caches individual job posting detail views (`jobs`).
- **PostgreSQL (`:5432`)**: Stores `job_postings`, `job_required_skills`, and `job_applications` tables.

## 4. Package & Folder Structure Explanation

### `com.careeros.job.controller`
- **Why Controller exists**: Exposes REST API endpoints (`JobPostingController`, `JobApplicationController`) for job search, application submission, and status pipeline updates.

### `com.careeros.job.service` & `impl`
- **Why Service exists**: Enforces business rules (preventing double candidate application to the same job), transactional limits (`@Transactional`), Redis cache eviction, and Kafka event publishing.

### `com.careeros.job.repository`
- **Why Repository exists**: Provides data access for job listings and application states, featuring multi-criteria search queries.

### `com.careeros.job.entity`
- **Why Entity exists**: Defines JPA mappings for `JobPosting` and `JobApplication` with soft delete (`is_deleted`) and optimistic locking (`@Version`).

### `com.careeros.job.dto`
- **Why DTO exists**: Separates external REST request/response contracts from JPA entity structures.

### `com.careeros.job.config`
- **Why Config exists**: Sets up `RedisConfig`, `KafkaProducerConfig`, and Swagger `OpenApiConfig`.

## 5. End-to-End Request Flow
1. Candidate submits `POST /api/v1/applications` with `jobId` and `candidateId`.
2. `JobApplicationController` validates request with `@Valid`.
3. `JobApplicationServiceImpl.applyForJob` verifies job existence and checks if candidate already applied (`existsByJobIdAndCandidateId`).
4. `JobApplication` entity saved to PostgreSQL `job_applications` table.
5. `JobEvent` (`JOB_APPLIED`) published to Kafka topic `careeros.job.events`.
6. Response returned with status `201 Created`.

## 6. Senior Interview Questions & Production Patterns
- **Q: How do you prevent race conditions when thousands of candidates apply for a limited job opening?**
  - *Answer*: Database uniqueness constraint `CONSTRAINT uk_job_candidate UNIQUE (job_id, candidate_id)` enforces application uniqueness at the SQL level, while optimistic locking (`@Version`) prevents dirty writes during pipeline status updates.

## 7. Future AI / ML / NLP & AWS Expansion Scope
- **Semantic Candidate Matching**: Match candidate embedding vectors against job requirement embeddings in AWS OpenSearch.
- **AWS Bedrock Job Match Scoring**: Automatically score candidate fit using Claude 3.5 Sonnet upon application submission.
