# Job & Application Microservice — Architecture & System Design Document

## 1. Executive System Architecture

The **CareerOS Job & Application Microservice** (`job-service`) is built using **Spring Boot 3**, **Java 21**, **PostgreSQL**, **Flyway**, **Redis**, **Kafka**, and **Clean Architecture**. It acts as the core workflow orchestrator for career opportunities, enabling two complete coexisting application models:
1. **Traditional Manual Job Search & Application**
2. **AI-Powered Conversational Job Search & AI-Assisted Job Application**

```mermaid
graph TD
    Client[React 19 Frontend] -->|REST API| Gateway[Spring Cloud API Gateway :8080]
    
    subgraph Job & Application Microservice Stack (:8083)
        Gateway -->|Eureka Discovery Routing| Controllers[REST Controllers: Jobs, Applications, AI Search, AI Apply]
        Controllers --> Services[Domain Services: JobSearchService, ApplicationService, AIJobSearchService]
        
        Services --> Specs[JPA Specifications: Dynamic Filter Engine]
        Services --> DB[(PostgreSQL: job_db)]
        Services --> RedisCache[(Redis Cache: 1-hr TTL)]
        
        Services --> FeignAI[Feign REST Client: AI-Agent Service :8000]
        Services --> FeignProfile[Feign REST Client: Profile Service :8082]
        Services --> KafkaProducer[Kafka Event Producer]
    end
    
    FeignAI -->|RAG & Intent Extraction| AIAgent[FastAPI AI-Agent Service]
    FeignProfile -->|Retrieve Candidate Profile| ProfileService[Profile Service]
    KafkaProducer -->|Stream Audit Events| Kafka[Apache Kafka :9092]
    Kafka -->|Consume Events| AuditService[Audit Service :8085]
    Kafka -->|Consume Notification Events| NotificationService[Notification Service :8084]
```

---

## 2. Sequence Diagrams for Core Workflows

### Workflow 1: Traditional Manual Job Search & Filtering

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Gateway as API Gateway (:8080)
    participant JobService as Job Service (:8083)
    participant Redis as Redis Cache
    participant DB as PostgreSQL (job_db)
    
    User->>Gateway: GET /api/v1/jobs/search?keyword=Java&location=Bangalore&remote=true
    Gateway->>JobService: Forward authenticated request
    JobService->>Redis: Check cache for query hash
    alt Cache Hit
        Redis-->>JobService: Return cached Page<JobPostingDTO>
    else Cache Miss
        JobService->>DB: Execute JobSpecification query (GIN & B-tree indexes)
        DB-->>JobService: Return paginated JobPosting entities
        JobService->>Redis: Store results in cache (TTL 60m)
    end
    JobService-->>User: 200 OK (Paginated Job List)
```

---

### Workflow 2: AI-Powered Conversational Search (RAG)

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Gateway as API Gateway (:8080)
    participant JobService as Job Service (:8083)
    participant AIAgent as FastAPI AI-Agent (:8000)
    participant DB as PostgreSQL (job_db)
    participant Kafka as Apache Kafka (:9092)
    
    User->>Gateway: POST /api/v1/jobs/ai-search { prompt: "I need Spring Boot roles in Bangalore" }
    Gateway->>JobService: Forward request
    JobService->>AIAgent: POST /api/v1/rag/search (Intent parsing & RAG similarity search)
    AIAgent-->>JobService: Extracted filters & RAG matching job IDs with match explanations
    JobService->>DB: Fetch job details for retrieved job IDs
    JobService->>Kafka: Publish AI_JOB_SEARCH event
    JobService-->>User: 200 OK (Ranked jobs + AI Match Explanation)
```

---

### Workflow 3: AI-Assisted Job Application ("Apply with AI")

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Gateway as API Gateway (:8080)
    participant JobService as Job Service (:8083)
    participant ProfileService as Profile Service (:8082)
    participant AIAgent as FastAPI AI-Agent (:8000)
    participant DB as PostgreSQL (job_db)
    participant Kafka as Apache Kafka (:9092)
    
    User->>Gateway: POST /api/v1/jobs/ai-apply { jobId: "job-101" }
    Gateway->>JobService: Forward request
    JobService->>ProfileService: GET /api/v1/profiles/me (Fetch resume, skills, experience)
    ProfileService-->>JobService: Candidate Profile Data
    JobService->>AIAgent: POST /api/v1/resume/ats-score & /improve (ATS scoring & Cover Letter generation)
    AIAgent-->>JobService: ATS score (88%), ATS Resume, & Personalized Cover Letter
    JobService-->>User: 200 OK (Generated Package for User Approval)
    
    User->>Gateway: POST /api/v1/jobs/ai-apply/confirm { packageId: "pkg-101" }
    Gateway->>JobService: Submit confirmed application
    JobService->>DB: Save JobApplication (status: APPLIED, ai_assisted: true)
    JobService->>Kafka: Publish JOB_APPLIED & AI_APPLICATION_COMPLETED events
    JobService-->>User: 201 Created (Application Submitted)
```

---

## 3. Database Schema Specifications & Flyway Migrations

The microservice manages five core tables initialized via Flyway (`V1__init_job_schema.sql`):

| Table Name | Primary Purpose | Key Columns & Indexes |
| :--- | :--- | :--- |
| `job_postings` | Stores job listings, company data, and requirements | `id`, `company_name`, `title`, `description`, `min_salary`, `max_salary`, `experience_level`, `employment_type`, `is_remote`, `status`. **Indexes**: `idx_job_company`, `idx_job_location`, `idx_job_salary`. |
| `job_required_skills` | Skills associated with each job posting | `job_id`, `skill_name`. **Index**: `idx_job_skills` on `skill_name`. |
| `job_applications` | Stores user application tracking & ATS scores | `id`, `job_id`, `candidate_id`, `status`, `resume_url`, `cover_letter`, `ats_score`, `ai_assisted`. **Index**: `idx_app_candidate_job` (unique per candidate/job). |
| `job_bookmarks` | Candidate saved/bookmarked jobs | `candidate_id`, `job_id`, `created_at`. |
| `recent_job_views` | Tracks candidate recently viewed job postings | `candidate_id`, `job_id`, `viewed_at`. |

---

## 4. Production Architectural Best Practices

1. **Zero-AI Logic in Job Service**: All NLP, vector search, embeddings, ATS scoring, and LLM prompt engineering are strictly encapsulated inside `AI-Agent`. `job-service` purely orchestrates business transactions.
2. **Dynamic JPA Specifications**: Type-safe dynamic filtering using Spring Data JPA `Specification<JobPosting>` avoiding raw SQL injection risks.
3. **Optimistic Locking**: Enforced via `@Version` field on `JobPosting` and `JobApplication` to prevent race conditions during concurrent updates.
4. **Soft Delete**: Implemented via `@SQLDelete(sql = "UPDATE job_postings SET deleted = true WHERE id = ?")` and `@Where(clause = "deleted = false")`.
