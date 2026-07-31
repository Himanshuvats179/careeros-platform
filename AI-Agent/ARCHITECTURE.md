# CareerOS AI Agent Microservice — Enterprise Production Architecture (v2.0)

## 1. Staff Architectural Review & System Blueprint
Designed by: **Staff AI Platform Architect**  
Target Environment: **Production High-Throughput Microservice Stack**  
Design Philosophy: **Clean Architecture, Domain-Driven Design, Zero-Trust Guardrails & SOLID Principles**

---

## 2. Improved End-to-End Enterprise Architecture Diagram

```mermaid
graph TD
    Client[React 19 Frontend / Spring Cloud API Gateway :8080] -->|HTTPS REST / Kafka| Gateway[FastAPI Microservice Entrypoint :8000]
    
    subgraph Edge & Performance Layer
        Gateway --> Auth[JWT & Rate Limiter Middleware]
        Auth --> Cache[Semantic Cache: Redis Vector Similarity]
        Cache -->|Cache Hit: O1 Latency| Client
    end
    
    subgraph Multi-Agent Orchestration & Workflow Engine
        Cache -->|Cache Miss| Orchestrator[AgentOrchestrator: State Graph Manager]
        Orchestrator --> Planner[TaskPlanner: Goal Decomposition & DAG Reasoning]
        Planner --> ToolRegistry[ToolRegistry: Skill Extractor, Resume Parser, Salary API]
        
        Orchestrator --> MemoryManager[Agent Memory Manager: Short-Term + Long-Term Vector History]
    end

    subgraph 2-Stage Hybrid RAG Pipeline
        Orchestrator --> RAGPipeline[RAG Pipeline Engine]
        RAGPipeline --> Embeddings[Embedding Interface: SentenceTransformers / BAAI bge-small / OpenAI]
        Embeddings --> VectorStore[(ChromaDB / FAISS / OpenSearch)]
        VectorStore -->|Stage 1: Top-K Vector Retrieval| Candidates[Retrieved Chunks]
        Candidates --> Reranker[Stage 2: CrossEncoder Reranker ms-marco-MiniLM-L-6]
        Reranker --> RAGContext[Refined RAG Context Assembly]
    end

    subgraph LLM Provider & Execution Abstraction
        Orchestrator --> AgentExecution[Domain Agents: Resume, Career, Interview Agents]
        RAGContext --> AgentExecution
        AgentExecution --> PromptMgr[Prompt Manager & Version Registry v1.2]
        AgentExecution --> LLMFactory[Provider-Agnostic LLM Factory]
        
        LLMFactory --> LocalLLM[Ollama Local Model]
        LLMFactory --> OpenAI[OpenAI GPT-4o]
        LLMFactory --> Bedrock[AWS Bedrock Claude 3.5]
    end

    subgraph Evaluation, Guardrails & Quality Assurance
        AgentExecution --> Reflection[ReflectionAgent: Grounding & Self-Evaluation]
        Reflection --> Guardrails[Guardrails Engine: PII, Safety, Confidence Validator]
        Guardrails --> Evaluator[Continuous Evaluation Framework: NDCG, MRR, Faithfulness]
    end

    subgraph Asynchronous & Telemetry Layer
        Orchestrator --> Telemetry[OpenTelemetry & Prometheus Metrics Engine]
        Orchestrator --> AsyncQueue[Celery / Redis Async Task Queue]
        Orchestrator --> KafkaProducer[Kafka AI Event Streaming Producer]
        KafkaProducer -->|Publish Audit & Analytics Events| Kafka[Apache Kafka Cluster :9092]
        AsyncQueue --> S3Storage[AWS S3 / Cloud Storage Service]
    end
```

---

## 3. Modular Folder Structure & SOLID Component Mapping

```
AI-Agent/
├── ARCHITECTURE.md                  # Enterprise Architecture Blueprint
├── SERVICE_GUIDE.md                 # Service Guide & Production Guide
├── Dockerfile                       # Multi-stage Container Specification
├── app/
│   ├── main.py                      # FastAPI Application Entrypoint
│   ├── config/                      # Environment & Feature Flag Management
│   │   └── settings.py
│   ├── api/                         # Presentation Layer (REST Routers)
│   │   └── v1/
│   │       ├── router.py            # Central Aggregator
│   │       ├── rag_router.py        # Ingestion, Vector Search & RAG Queries
│   │       ├── resume_router.py     # Resume Analysis, Optimization & ATS Scoring
│   │       ├── career_router.py     # Roadmap Generation & Skill Gap Analysis
│   │       ├── interview_router.py  # Interview Questions & STAR Grading
│   │       └── chat_router.py       # Conversational AI Assistant
│   ├── orchestrator/                # Orchestration Layer (State & DAG Engine)
│   │   └── agent_orchestrator.py
│   ├── planner/                     # Reasoning & Goal Decomposition
│   │   └── task_planner.py
│   ├── agents/                      # Domain AI Agents
│   │   ├── resume_agent.py
│   │   ├── career_agent.py
│   │   ├── interview_agent.py
│   │   └── reflection_agent.py      # Self-Evaluation Agent
│   ├── rag/                         # RAG Engine
│   │   ├── pipeline.py              # 2-Stage Retrieval & Reranking Engine
│   │   ├── chunker.py               # Recursive Document Splitter
│   │   └── preprocessor.py          # Metadata Tagging & Sanitization
│   ├── embeddings/                  # Embedding Abstraction Layer
│   │   ├── base.py                  # Abstract Base Interface
│   │   └── sentence_transformer.py  # Local SentenceTransformers Implementation
│   ├── vector_store/                # Vector Database Abstraction
│   │   ├── base.py                  # Abstract Interface
│   │   └── chroma_store.py          # ChromaDB Storage Engine
│   ├── ml/                          # Machine Learning & Reranking Layer
│   │   ├── reranker.py              # CrossEncoder Reranker
│   │   └── registry.py              # Custom Model Registry Interface
│   ├── cache/                       # Performance Layer
│   │   └── semantic_cache.py        # Redis Vector Similarity Semantic Cache
│   ├── memory/                      # Memory Layer
│   │   └── conversation_memory.py  # Short-Term Session & Long-Term Context
│   ├── prompt_manager/              # Prompt Governance
│   │   ├── templates.py             # Enterprise System Prompts
│   │   └── version_manager.py       # Prompt Version Control
│   ├── tools/                       # Tool Execution Registry
│   │   └── registry.py              # Dynamic Function Execution Registry
│   ├── evaluation/                  # Quality & Compliance Layer
│   │   ├── guardrails.py            # Safety, PII & Confidence Validator
│   │   └── feedback.py              # HITL Feedback & RAG Faithfulness Evaluator
│   ├── schemas/                     # Data Transfer Objects (DTOs)
│   │   ├── rag_schemas.py
│   │   └── resume_schemas.py
│   ├── services/                    # Application Orchestration Services
│   │   ├── ai_service.py            # High-level Orchestration Service
│   │   ├── llm_factory.py           # Provider-Agnostic LLM Engine
│   │   ├── aws_s3_service.py        # Object Storage Client
│   │   └── kafka_producer.py        # Event Telemetry Streaming
│   └── utils/                       # Infrastructure Utilities
│       ├── logger.py
│       └── exceptions.py
```

---

## 4. Key Production Architectural Improvements & Patterns

### 1. Semantic Caching (`app/cache/semantic_cache.py`)
- **Problem**: Repetitive user queries hit LLM APIs, increasing latency ($2-5\text{s}$) and operational cost.
- **Solution**: Vector similarity semantic cache using Redis Vector Search. Queries with cosine similarity $\ge 0.95$ return cached responses instantaneously ($O(1)$ latency, $<20\text{ms}$).

### 2. Custom Model & Prompt Registry (`app/ml/registry.py` & `app/prompt_manager/version_manager.py`)
- **Problem**: Hardcoded prompts and models cause regression issues during model upgrades.
- **Solution**: Decoupled Model & Prompt Registry enforcing explicit semantic versioning (`v1.0.0`, `v1.1.0`) and fallback rules.

### 3. Continuous RAG Evaluation & Faithfulness (`app/evaluation/feedback.py`)
- **Problem**: Lack of visibility into RAG retrieval quality and LLM hallucination rate.
- **Solution**: Evaluation metrics tracking **Retrieval Precision (NDCG@K)**, **Context Relevance**, and **Faithfulness Score** before emitting guardrail approvals.

### 4. Asynchronous Task Processing (`Celery / Redis Worker`)
- **Problem**: Synchronous document ingestion blocks HTTP threads for large resume uploads.
- **Solution**: Offload heavy embedding generation and document ingestion tasks to background worker pools.

---

## 5. Prioritized Production Implementation Roadmap

```
[Phase 1: Foundation] ──► [Phase 2: RAG Precision] ──► [Phase 3: Performance & Cache] ──► [Phase 4: Observability & MLOps]
• Clean Architecture     • CrossEncoder Reranker       • Redis Semantic Cache       • OpenTelemetry Tracing
• Provider-Agnostic LLM  • 2-Stage RAG Pipeline        • Asynchronous Celery Jobs   • RAG Faithfulness Metrics
• Multi-Agent Planner    • Grounded Citations          • HITL Feedback Loop         • Kafka Telemetry Audit
```
