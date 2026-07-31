# 🚀 CareerOS — Enterprise AI-Powered Career & Job Platform

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![FastAPI](https://img.shields.io/badge/FastAPI-0.110.0-009688?style=for-the-badge&logo=fastapi&logoColor=white)](https://fastapi.tiangolo.com/)
[![Python 3.12](https://img.shields.io/badge/Python-3.12-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://www.python.org/)
[![React 19](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Docker](https://img.shields.io/badge/Docker-24.0-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-3.7-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)](https://kafka.apache.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.2-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

**CareerOS** is an enterprise-grade, event-driven AI platform built with **10 Spring Boot 3 Java 21 microservices**, a **FastAPI Python 3.12 Multi-Agent AI Service**, a **2-Stage RAG Pipeline**, **Apache Kafka**, **RabbitMQ**, **Redis**, **PostgreSQL**, **ChromaDB**, and a **React 19 Frontend**.

---

## 1. Master System Architecture Blueprint

```mermaid
graph TD
    Client[React 19 Frontend :3000] -->|HTTPS REST / JWT| Gateway[Spring Cloud API Gateway :8080]
    Gateway -->|Eureka Discovery| Eureka[Eureka Service Registry :8761]
    
    subgraph Java Spring Boot Microservice Ecosystem
        Gateway --> Auth[Auth Service :8081]
        Gateway --> Profile[Profile Service :8082]
        Gateway --> Job[Job Service :8083]
        Gateway --> Notification[Notification Service :8084]
        Gateway --> Audit[Audit Service :8085]
        
        Profile -->|Read/Write| ProfileDB[(PostgreSQL - profile_db)]
        Job -->|Read/Write| JobDB[(PostgreSQL - job_db)]
        Auth -->|Read/Write| AuthDB[(PostgreSQL - auth_db)]
        Audit -->|Read/Write| AuditDB[(PostgreSQL - audit_db)]
        
        Profile -->|Cache| Redis[(Redis Cluster :6379)]
        Job -->|Cache| Redis
    end
    
    subgraph FastAPI Multi-Agent AI Microservice (:8000)
        Gateway -->|HTTP Routing /api/v1/ai| AIAgent[FastAPI Core Orchestrator]
        Job -->|Feign REST Client| AIAgent
        
        AIAgent --> Planner[TaskPlanner & ToolRegistry]
        AIAgent --> RAG[2-Stage RAG Pipeline]
        AIAgent --> SemanticCache[Vector Semantic Cache: Redis]
        
        RAG --> Embeddings[SentenceTransformers all-MiniLM-L6-v2]
        Embeddings --> Chroma[(Chroma Vector DB)]
        RAG --> CrossEncoder[CrossEncoder Reranker ms-marco-MiniLM-L-6]
        
        AIAgent --> LLMFactory[LLM Provider Factory]
        LLMFactory --> LocalOllama[Ollama Local Llama 3]
        LLMFactory --> OpenAI[OpenAI GPT-4o]
        LLMFactory --> Bedrock[AWS Bedrock Claude 3.5]
    end
    
    subgraph Event Streaming & Messaging Pipelines
        Auth -->|Publish USER_REGISTERED| Kafka[Apache Kafka :9092]
        Profile -->|Publish PROFILE_UPDATED| Kafka
        Job -->|Publish JOB_APPLIED| Kafka
        AIAgent -->|Publish AI_EVENTS| Kafka
        
        Kafka -->|Consume Events| Audit
        Kafka -->|Consume Notifications| Notification
        Notification -->|Queue Notifications| RabbitMQ[RabbitMQ :5672]
        Notification -->|SMTP Email Sandbox| Mailpit[Mailpit :8025]
    end
```

---

## 2. Microservice Ecosystem Architecture Diagram

```mermaid
graph LR
    subgraph Edge Layer
        GW[Spring Cloud API Gateway :8080]
        Config[Config Server :8888]
        Registry[Eureka Registry :8761]
    end

    subgraph Business Domains
        AuthService[Auth Service :8081]
        ProfileService[Profile Service :8082]
        JobService[Job Service :8083]
        NotifService[Notification Service :8084]
        AuditService[Audit Service :8085]
        AIService[FastAPI AI Agent :8000]
    end

    subgraph Persistence & Infrastructure
        AuthDB[(auth_db)]
        ProfileDB[(profile_db)]
        JobDB[(job_db)]
        AuditDB[(audit_db)]
        RedisCache[(Redis Cluster)]
        S3Bucket[AWS S3 Storage]
    end

    GW --> AuthService
    GW --> ProfileService
    GW --> JobService
    GW --> NotifService
    GW --> AuditService
    GW --> AIService

    AuthService --> AuthDB
    ProfileService --> ProfileDB
    ProfileService --> RedisCache
    ProfileService --> S3Bucket
    JobService --> JobDB
    JobService --> RedisCache
    AuditService --> AuditDB
```

---

## 3. Graphical Messaging Topology: Apache Kafka vs. RabbitMQ

```mermaid
graph TD
    subgraph Event Producers
        Auth[Auth Service]
        Profile[Profile Service]
        Job[Job Service]
        AI[AI Agent Service]
    end

    subgraph High-Throughput Event Streaming: Apache Kafka (:9092)
        TopicAuth[Topic: careeros.auth.events]
        TopicJob[Topic: careeros.job.events]
        TopicAI[Topic: careeros.ai.events]
        
        Auth --> TopicAuth
        Profile --> TopicAuth
        Job --> TopicJob
        AI --> TopicAI

        TopicAuth --> AuditConsumer[Audit Consumer: Audit Service]
        TopicJob --> AuditConsumer
        TopicAI --> AuditConsumer

        AuditConsumer --> DLQ[Kafka Dead Letter Queue .DLQ]
    end

    subgraph Reliable Task Queue: RabbitMQ (:5672)
        Exchange[Direct Exchange: careeros.notification.exchange]
        QueueEmail[Queue: careeros.notification.email.queue]
        DLXExchange[DLX: careeros.notification.dlx]
        
        Job -->|Trigger Email| Exchange
        Auth -->|Trigger Welcome Email| Exchange
        
        Exchange -->|Routing Key: notify.email| QueueEmail
        QueueEmail --> NotifConsumer[Notification Worker Service]
        QueueEmail -.->|Max Retries Exceeded| DLXExchange
        
        NotifConsumer --> Mailpit[Mailpit SMTP Sandbox :8025]
    end
```

---

## 4. AI Agent & 2-Stage RAG Pipeline Diagram

```mermaid
graph TD
    UserQuery[User Natural Query / Resume Text] -->|API Request| Orchestrator[AgentOrchestrator]
    
    subgraph Performance & Guardrails Engine
        Orchestrator --> SemanticCache{Semantic Vector Cache: Redis}
        SemanticCache -->|Cosine Sim ≥ 0.95 Hit| CacheReturn[Instant Response <20ms]
    end
    
    subgraph Multi-Agent Reasoning & Tool Execution
        SemanticCache -->|Cache Miss| TaskPlanner[TaskPlanner: Goal DAG Decomposition]
        TaskPlanner --> ToolRegistry[ToolRegistry: Skill Extractor, Resume Parser, Salary API]
    end
    
    subgraph 2-Stage Hybrid RAG Retrieval Engine
        TaskPlanner --> Stage1[Stage 1: Dense Vector Cosine Search top-k=10]
        Stage1 --> Embeddings[SentenceTransformers / BAAI bge-small Embeddings]
        Embeddings --> ChromaDB[(Chroma Vector Index)]
        ChromaDB --> TopCandidates[10 Retrieved Context Chunks]
        
        TopCandidates --> Stage2[Stage 2: CrossEncoder Reranker ms-marco-MiniLM-L-6]
        Stage2 --> RefinedContext[Top-3 Reranked Context Blocks]
    end
    
    subgraph LLM Inference & Reflection
        RefinedContext --> PromptAssembler[Grounded Prompt Context Assembly]
        PromptAssembler --> LLMFactory[Provider-Agnostic LLM Factory]
        
        LLMFactory --> Ollama[Ollama Local Llama 3]
        LLMFactory --> OpenAI[OpenAI GPT-4o]
        LLMFactory --> Bedrock[AWS Bedrock Claude 3.5]
        
        LLMFactory --> ReflectionAgent[ReflectionAgent: Grounding & Self-Evaluation]
        ReflectionAgent --> Guardrails[Guardrails Engine: PII & Confidence Check ≥ 70%]
    end
    
    Guardrails -->|Stream Telemetry| KafkaProducer[Kafka AI Event Producer: careeros.ai.events]
    Guardrails --> Output[Grounded Final Output]
```

---

## 5. Graphical React 19 Frontend Page Architecture

```mermaid
graph TD
    User[Candidate User] --> Shell[React 19 App Shell & Glassmorphic Layout]
    
    subgraph Authentication & State
        Shell --> AuthContext[AuthContext Provider]
    end
    
    subgraph Main Navigation Hubs
        Shell --> Dashboard[Dashboard Page: LinkedIn Jobs Feed & Apply with AI]
        Shell --> ProfilePage[My Profile Page: Skill Matrix & Resume Upload]
        Shell --> JobTracker[Job Tracker Page: Application Kanban Pipeline]
    end

    subgraph AI Career Intelligence Tools
        Shell --> ResumePage[AI Resume Optimizer: ATS Scoring & STAR Rewriter]
        Shell --> RoadmapPage[Career Roadmap Page: 9-Month Transition Graph]
        Shell --> InterviewPage[Mock Interview Coach: STAR Answer Grading]
        Shell --> AiChatPage[AI Assistant Chat: Conversational RAG Advisor]
    end

    subgraph Platform Telemetry & Management
        Shell --> AuditLogs[Audit Logs Page: Real-time Kafka Stream Viewer]
        Shell --> SettingsPage[Settings Page: Dark Mode & Demo Mode Switcher]
    end
```

---

## 6. Quick Start: Running the Full Stack

### **Option A: Run Full Stack with Docker Compose**
```bash
docker compose up -d --build
```
Dashboard endpoints:
- **React Frontend**: `http://localhost:3000`
- **Spring Cloud Gateway**: `http://localhost:8080`
- **FastAPI AI Agent OpenAPI Docs**: `http://localhost:8000/docs`
- **Eureka Service Registry**: `http://localhost:8761`
- **Kafka UI**: `http://localhost:8088`
- **Mailpit Email Sandbox**: `http://localhost:8025`

### **Option B: Run Services Locally for Development**
```bash
# 1. Package Java Microservices
cd Backend
.\mvnw.cmd clean package -DskipTests

# 2. Start FastAPI AI Agent
cd ../AI-Agent
py -m uvicorn app.main:app --reload --port 8000

# 3. Start React Frontend
cd ../Frontend
npm run dev
```

---

## 7. Master Documentation Architecture Files

- **[Master System Architecture Guide](file:///c:/Users/91741/Documents/Dream_NO.1/ARCHITECTURE.md)**
- **[AI Agent Master Architecture](file:///c:/Users/91741/Documents/Dream_NO.1/AI-Agent/ARCHITECTURE.md)**
- **[AI Agent Service Guide & Interview Q&A](file:///c:/Users/91741/Documents/Dream_NO.1/AI-Agent/SERVICE_GUIDE.md)**
- **[Job Service Architecture & Guide](file:///c:/Users/91741/Documents/Dream_NO.1/Backend/job-service/SERVICE_GUIDE.md)**
- **[Deployment & Production Operations](file:///c:/Users/91741/Documents/Dream_NO.1/DEPLOYMENT.md)**
