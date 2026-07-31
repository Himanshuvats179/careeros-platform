# Job & Application Microservice — Comprehensive Service Guide

## 1. Executive Summary (What this microservice does in 5 lines)
1. Provides enterprise job management, dynamic search, filtering, bookmarking, and application tracking for CareerOS.
2. Supports two complete coexisting workflows: **Traditional Manual Job Search** and **AI-Powered Conversational Job Search**.
3. Supports dual application models: **Manual Application** and **AI-Assisted Job Application ("Apply with AI")** with mandatory user approval.
4. Integrates seamlessly with **Auth Service** (JWT), **Profile Service** (Candidate Data), **AI-Agent** (RAG & ATS Scoring), **Notification Service** (RabbitMQ/Kafka emails), and **Audit Service** (Kafka telemetry).
5. Follows **Clean Architecture**, **Flyway DB Migrations**, **Redis Caching**, and **Dynamic JPA Specifications**.

---

## 2. Why This Microservice Exists
The Job & Application Microservice serves as the central operational hub for candidates finding and applying to career opportunities. It handles high-throughput job queries, dynamic filtering, application workflow state machines (`APPLIED` $\rightarrow$ `SCREENING` $\rightarrow$ `SHORTLISTED` $\rightarrow$ `INTERVIEW` $\rightarrow$ `OFFER` $\rightarrow$ `HIRED` / `REJECTED`), and integrates with AI agents without polluting core Java backend code with machine learning logic.

---

## 3. Package & Folder Structure Explanation

```
Backend/job-service/
├── ARCHITECTURE.md                  # Master architecture design document
├── SERVICE_GUIDE.md                 # Service operation guide & interview Q&A
├── Dockerfile                       # Production multi-stage Docker build
├── pom.xml                          # Maven dependency configuration
└── src/
    ├── main/
    │   ├── java/com/careeros/job/
    │   │   ├── JobServiceApplication.java   # Spring Boot entrypoint
    │   │   ├── config/                      # Configuration Beans
    │   │   │   ├── JpaConfig.java           # JPA Auditing Configuration
    │   │   │   ├── RedisConfig.java         # Redis Cache & Template Config
    │   │   │   ├── KafkaProducerConfig.java # Kafka Event Serialization
    │   │   │   ├── FeignConfig.java         # Feign Client Security Headers
    │   │   │   └── SecurityConfig.java      # Stateless JWT Security
    │   │   ├── controller/                  # REST Controllers (Presentation Layer)
    │   │   │   ├── JobPostingController.java
    │   │   │   ├── JobApplicationController.java
    │   │   │   ├── AIJobSearchController.java
    │   │   │   └── AIApplicationController.java
    │   │   ├── service/                     # Service Interfaces (Use Cases)
    │   │   │   ├── JobPostingService.java
    │   │   │   ├── JobApplicationService.java
    │   │   │   ├── AIJobSearchService.java
    │   │   │   └── AIApplicationService.java
    │   │   ├── service/impl/                # Service Implementations
    │   │   │   ├── JobPostingServiceImpl.java
    │   │   │   ├── JobApplicationServiceImpl.java
    │   │   │   ├── AIJobSearchServiceImpl.java
    │   │   │   └── AIApplicationServiceImpl.java
    │   │   ├── repository/                  # Spring Data JPA Repositories
    │   │   │   ├── JobPostingRepository.java
    │   │   │   ├── JobApplicationRepository.java
    │   │   │   ├── JobBookmarkRepository.java
    │   │   │   └── RecentJobViewRepository.java
    │   │   ├── specification/               # Dynamic Criteria Engine
    │   │   │   └── JobPostingSpecification.java
    │   │   ├── client/                      # REST Feign Clients
    │   │   │   ├── AIAgentClient.java       # Client for FastAPI AI-Agent (:8000)
    │   │   │   └── ProfileServiceClient.java# Client for Profile Service (:8082)
    │   │   ├── dto/                         # Data Transfer Objects
    │   │   │   ├── request/                 # Search & Application Request DTOs
    │   │   │   └── response/                # Response DTOs
    │   │   ├── entity/                      # JPA Domain Entities
    │   │   │   ├── JobPosting.java
    │   │   │   ├── JobApplication.java
    │   │   │   ├── JobBookmark.java
    │   │   │   └── RecentJobView.java
    │   │   ├── enums/                       # Domain Enums (ApplicationStatus, EmploymentType)
    │   │   ├── event/                       # Kafka Event Models
    │   │   │   ├── JobAppliedEvent.java
    │   │   │   └── AIJobSearchEvent.java
    │   │   ├── mapper/                      # MapStruct Entity-DTO Mappers
    │   │   │   └── JobMapper.java
    │   │   └── exception/                   # Global Exception Handler
    │   │       ├── GlobalExceptionHandler.java
    │   │       └── ResourceNotFoundException.java
    │   └── resources/
    │       ├── application.yml              # Microservice application settings
    │       └── db/migration/                # Flyway DB Migrations
    │           ├── V1__init_job_schema.sql
    │           └── V2__seed_demo_jobs.sql
```

---

## 4. End-to-End REST Endpoint Reference Table

| Method | Path | Summary | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/jobs/search` | Manual Job Search | Dynamic JPA filter by keyword, skills, company, salary, experience, remote. |
| `GET` | `/api/v1/jobs/{id}` | Job Details | Retrieves complete job details & records recent view activity. |
| `POST` | `/api/v1/jobs/{id}/bookmark` | Bookmark Job | Saves job to candidate bookmarked list. |
| `GET` | `/api/v1/jobs/recommendations` | Recommended Jobs | Fetches personalized jobs based on candidate skills. |
| `POST` | `/api/v1/jobs/ai-search` | AI Conversational Search | Natural language RAG search ("I need Spring Boot roles in Bangalore"). |
| `POST` | `/api/v1/jobs/apply` | Manual Application | Submits manual job application with candidate resume URL. |
| `POST` | `/api/v1/jobs/ai-apply` | Apply with AI (Package) | Calls AI-Agent to analyze resume, compute ATS score, & generate cover letter. |
| `POST` | `/api/v1/jobs/ai-apply/confirm` | Confirm AI Application | User approves AI package and submits application to PostgreSQL & Kafka. |
| `GET` | `/api/v1/applications/my-applications` | Application History | Lists candidate submitted applications with status progression. |

---

## 5. Senior Engineering Interview Q&A

### Q1: Why separate AI logic from the Job Service into a dedicated AI-Agent microservice?
**Answer**: To uphold **Single Responsibility Principle (SRP)** and **Clean Architecture**. The `job-service` is written in Java 21/Spring Boot 3 and handles high-throughput relational transactions, state machines, and dynamic filtering. AI workloads (Python, PyTorch, LangChain, ChromaDB, vector embeddings) require specialized ML dependencies and run on GPU/CPU container instances. Keeping them separate prevents dependency bloat and allows independent horizontal scaling.

### Q2: How do you handle dynamic multi-field filtering without writing raw SQL queries?
**Answer**: We use **Spring Data JPA Specifications** (`Specification<JobPosting>`). It provides a type-safe API building `Predicate` instances dynamically based on non-null request parameters (keyword, skills, salary range, experience level, remote flag), leveraging database B-tree and GIN indexes safely without SQL injection vulnerabilities.

### Q3: How is user approval enforced during AI-assisted job applications?
**Answer**: Through a **Two-Step Application Package Workflow**. In Step 1 (`POST /api/v1/jobs/ai-apply`), the system collects candidate profile and job details, calls the AI-Agent to generate an ATS-optimized package, and returns it to the user for review. The application status remains `DRAFT`. In Step 2 (`POST /api/v1/jobs/ai-apply/confirm`), the candidate explicitly reviews and approves the package, which transitions the status to `APPLIED` and emits Kafka telemetry events.
