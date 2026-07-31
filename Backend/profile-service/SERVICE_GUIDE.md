# Profile Service - Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Manages candidate personal information, headlines, bios, and profile pictures.
2. Tracks skills, work experience, education, projects, certifications, and languages.
3. Provides secure resume upload and avatar file storage management.
4. Integrates Redis caching to serve candidate profile queries at low latency.
5. Publishes profile update events to Apache Kafka for audit logging and AI recommendations.

## 2. Why This Service Exists
The Profile Service serves as the single source of truth for candidate data in CareerOS. It abstracts candidate resume details into structured database domain entities, allowing AI agents, recruiters, and search engines to query skill sets, calculate ATS compatibility scores, and generate tailored career progression roadmaps.

## 3. Microservice Dependencies & Interactions
- **API Gateway (`:8080`)**: Routes incoming client HTTP requests to Profile Service after validating JWT tokens.
- **Service Registry (`:8761`)**: Eureka discovery server registering Profile Service instances dynamically.
- **Redis (`:6379`)**: Caches candidate profile responses with a 1-hour TTL to reduce database read pressure.
- **PostgreSQL (`:5432`)**: Persists profile aggregates with versioned schema migrations managed by Flyway.
- **Kafka (`:9092`)**: Listens for `CAREEROS.PROFILE.EVENTS` published by Profile Service.
- **AI Agent Service (`:8000`)**: Reads candidate skills and experience to analyze resumes and generate career roadmaps.

## 4. Package & Folder Structure Explanation

### `com.careeros.profile.controller`
- **Why Controller exists**: Exposes RESTful HTTP endpoints (`/api/v1/profiles`, `/api/v1/resumes`) for external clients. Validates request payloads with `@Valid`.
- **Production Pattern**: Uses standard `ApiResponse<T>` response wrappers, explicit HTTP status codes (`200 OK`, `201 Created`, `404 Not Found`), and OpenAPI Swagger annotations.

### `com.careeros.profile.service` & `impl`
- **Why Service exists**: Encapsulates business logic, transactional boundaries (`@Transactional`), caching rules (`@Cacheable`, `@CacheEvict`), and calculations (e.g. profile completion percentage algorithm).
- **Production Pattern**: Implements soft-deletion (`is_deleted`), optimistic locking (`@Version`), and custom exception handling.

### `com.careeros.profile.repository`
- **Why Repository exists**: Provides data access using Spring Data JPA Repositories and custom JPA Specifications for dynamic multi-criteria candidate searching.

### `com.careeros.profile.entity`
- **Why Entity exists**: Maps PostgreSQL tables (`profiles`, `skills`, `experiences`, `educations`, `projects`, `certifications`, `languages`) to Java domain objects using JPA annotations. Inherits `BaseAuditEntity` for automatic `createdAt`, `updatedAt`, `version`, and `isDeleted` tracking.

### `com.careeros.profile.dto`
- **Why DTO exists**: Decouples API contract data models from database entities. Prevents over-posting, protects private database fields, and provides tailored view models for request/response payloads.

### `com.careeros.profile.mapper`
- **Why Mapper exists**: Converts Entity domain objects into Response DTOs and vice versa. Computes dynamic properties like completion percentage during mapping.

### `com.careeros.profile.config`
- **Why Config exists**: Configures Redis JSON serialization, OpenAPI Swagger UI metadata, and file upload size/MIME type boundaries.

## 5. Important Classes Summary
- `Profile.java`: Aggregate Root entity containing child collections (`skills`, `experiences`, etc.).
- `ProfileServiceImpl.java`: Core service containing profile CRUD, Redis caching logic, and file uploads.
- `ProfileController.java`: REST controller handling profile endpoints.
- `ResumeController.java`: REST controller dedicated to resume PDF/Docx uploads.
- `ProfileRepository.java`: Custom JPA specification repository for dynamic multi-attribute candidate queries.

## 6. End-to-End Request Flow
1. Client sends `PUT /api/v1/profiles/{id}` with updated skills.
2. API Gateway validates JWT, attaches `X-User-Id` and `X-Correlation-Id`, and forwards request.
3. `ProfileController` receives request, triggers `@Valid` validation, and calls `ProfileService.updateProfile`.
4. `ProfileServiceImpl` starts transaction (`@Transactional`), fetches profile from `ProfileRepository`, verifies optimistic lock (`@Version`), applies updates, recalculates completion percentage, and saves to PostgreSQL.
5. `@CacheEvict` clears the stale candidate entry from Redis cache `profiles`.
6. Event is published to Kafka topic `careeros.profile.events`.
7. `ProfileMapper` converts saved entity to `ProfileResponse` and returns `200 OK`.

## 7. Senior Interview Questions & Production Patterns
- **Q: How do you prevent lost updates when two users edit a profile simultaneously?**
  - *Answer*: We use **Optimistic Locking** via JPA `@Version` column. If a concurrent update changes the version number in PostgreSQL, an `OptimisticLockingFailureException` is thrown and handled gracefully by `GlobalExceptionHandler`.
- **Q: Why use Soft Delete instead of hard deleting profile data?**
  - *Answer*: Soft deletion (`@SQLDelete`, `@Where(clause = "is_deleted = false")`) preserves historical candidate data for compliance, analytics, and machine learning training pipelines while hiding deleted records from active queries.

## 8. Future AI / ML / NLP & AWS Expansion Scope
- **OCR & Resume Parsing**: Integrate AWS Textract or PyMuPDF to extract text from PDF/Docx resumes automatically upon upload.
- **Embedding Generation**: Export candidate profiles to OpenAI / HuggingFace embedding models (`text-embedding-3-small`) for Vector DB indexing (Chroma / AWS OpenSearch).
- **AWS S3 Direct Upload**: Replace local file storage with AWS S3 Presigned URLs for direct client-to-S3 uploads.
- **Semantic Candidate Search**: Connect candidate skills to AWS OpenSearch for BM25 + Vector Hybrid search.
