# CareerOS AI Agent Microservice — Comprehensive Service Guide

## 1. Executive Summary (What this service does in 5 lines)
1. Exposes 16 production REST API endpoints for RAG retrieval, resume optimization, ATS scoring, career roadmaps, and mock interview evaluation.
2. Implements Clean Architecture with 19 modular packages separating API, Agents, RAG, Embeddings, Vector Store, Memory, Tools, and Guardrails.
3. Provides open-source defaults (**SentenceTransformers**, **ChromaDB**, **Ollama**) with zero-code switching to **OpenAI**, **AWS Bedrock**, or **Azure OpenAI**.
4. Features a 2-stage RAG pipeline (Vector Search + CrossEncoder Reranking) for zero-hallucination grounded response generation.
5. Emits real-time event telemetry (`careeros.ai.events`) to Apache Kafka for centralized audit logging and analytics.

---

## 2. Why This Service Exists
The AI Agent microservice decouples complex LLM prompt engineering, vector search indexing, and multi-agent reasoning from Java Spring Boot backend microservices. It provides a dedicated Python 3.12 FastAPI microservice optimized for high-throughput AI workloads, embeddings, and machine learning inference.

---

## 3. Microservice Dependencies & External Interactions
- **API Gateway (`:8080`)**: Intercepts JWT tokens and forwards client HTTP requests under `/api/v1/ai` to FastAPI (`:8000`).
- **ChromaDB (`:8001` or Local persistent `./chroma_db`)**: Vector database storing document embeddings.
- **Apache Kafka (`:9092`)**: Kafka cluster receiving streamed domain events on topic `careeros.ai.events`.
- **AWS S3 (`careeros-ai-artifacts`)**: Cloud object store for candidate resumes and generated career documents.
- **Ollama / OpenAI / AWS Bedrock**: Large Language Model inference endpoints.

---

## 4. Complete Package & Folder Structure Guide

```
AI-Agent/
├── ARCHITECTURE.md                  # Master architecture design document
├── SERVICE_GUIDE.md                 # Service operation guide & interview Q&A
├── Dockerfile                       # Production multi-stage Docker build
├── requirements.txt                 # Python dependencies
├── app/
│   ├── main.py                      # FastAPI application entrypoint
│   ├── config/                      # Environment settings & feature flags
│   │   └── settings.py
│   ├── api/                         # REST API Endpoints
│   │   └── v1/
│   │       ├── router.py            # Central router aggregator
│   │       ├── rag_router.py        # RAG ingestion, search & query endpoints
│   │       ├── resume_router.py     # Resume analysis, rewrite & ATS scoring
│   │       ├── career_router.py     # Career roadmap & skill gap analysis
│   │       ├── interview_router.py  # Question generation & STAR evaluation
│   │       └── chat_router.py       # Conversational AI assistant
│   ├── orchestrator/                # Multi-Agent Workflow State Orchestrator
│   │   └── agent_orchestrator.py
│   ├── planner/                     # Task Decomposition & Goal Reasoning
│   │   └── task_planner.py
│   ├── agents/                      # Domain AI Agents
│   │   ├── resume_agent.py
│   │   ├── career_agent.py
│   │   ├── interview_agent.py
│   │   └── reflection_agent.py      # Reflection & Self-Evaluation Agent
│   ├── rag/                         # RAG Engine
│   │   └── pipeline.py              # 2-Stage Ingestion & Retrieval Pipeline
│   ├── embeddings/                  # Provider-Agnostic Embeddings
│   │   ├── base.py                  # Abstract Base Interface
│   │   └── sentence_transformer.py  # Local SentenceTransformers provider
│   ├── vector_store/                # Vector Database Abstraction
│   │   ├── base.py                  # Abstract Vector Store Interface
│   │   └── chroma_store.py          # ChromaDB implementation
│   ├── ml/                          # Machine Learning & Reranking
│   │   └── reranker.py              # CrossEncoder Reranker
│   ├── memory/                      # Conversation & Working Memory
│   │   └── conversation_memory.py
│   ├── prompt_manager/              # System Prompt Engineering & Templates
│   │   └── templates.py
│   ├── tools/                       # Modular Tool Registry
│   │   └── registry.py              # Tool Executor & Function Registry
│   ├── evaluation/                  # Response Validation & Guardrails
│   │   └── guardrails.py            # Confidence Scoring & Output Validator
│   ├── schemas/                     # Pydantic Payload Data Models
│   │   └── rag_schemas.py
│   ├── services/                    # High-Level Services
│   │   ├── ai_service.py            # Application Orchestration Service
│   │   ├── llm_factory.py           # Provider-Agnostic LLM Factory
│   │   ├── aws_s3_service.py        # AWS S3 Storage Service
│   │   └── kafka_producer.py        # Kafka AI Event Producer
│   └── utils/                       # Shared Utilities
│       ├── logger.py
│       └── exceptions.py
```

---

## 5. End-to-End Endpoints Reference Table

| Method | Path | Summary | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/rag/ingest` | Document Ingestion | Ingests candidate resume or job document into vector index. |
| `POST` | `/api/v1/rag/search` | Vector Similarity Search | Searches vector store with 2-stage CrossEncoder reranking. |
| `POST` | `/api/v1/rag/query` | Grounded RAG Query | Generates LLM answer backed by retrieved document citations. |
| `POST` | `/api/v1/resume/analyze` | Resume Analysis | Identifies strengths, weaknesses, formatting score, and skills. |
| `POST` | `/api/v1/resume/improve` | Resume Optimization | Generates quantifiable STAR bullet point rewrites. |
| `POST` | `/api/v1/resume/ats-score` | ATS Keyword Match | Computes ATS keyword match percentage and gap recommendations. |
| `POST` | `/api/v1/career/roadmap` | Career Roadmap | Generates step-by-step skill transition milestones. |
| `POST` | `/api/v1/career/skill-gap` | Skill Gap Analysis | Compares current candidate skills against target role requirements. |
| `POST` | `/api/v1/interview/questions` | Interview Question Gen | Generates technical & behavioral questions based on target role. |
| `POST` | `/api/v1/interview/evaluate` | STAR Answer Evaluation | Grades candidate interview answers against STAR criteria. |
| `POST` | `/api/v1/chat/message` | AI Career Assistant | Conversational RAG agent with context memory and follow-up prompts. |

---

## 6. Senior Engineering Interview Q&A

### Q1: How do you prevent LLM hallucinations when building enterprise AI tools?
**Answer**: We employ a **2-stage RAG pipeline** combined with a **Reflection & Guardrails Agent**. First, dense vector retrieval fetches top candidate chunks from ChromaDB. Next, a **CrossEncoder Reranker** scores the chunks for semantic relevance. Finally, the system prompt explicitly constrains the LLM to generate answers strictly from the provided context blocks, and the `ReflectionAgent` verifies factual grounding before returning the response.

### Q2: Why use a 2-stage retrieval approach (Vector Search + CrossEncoder Reranking)?
**Answer**: Bi-encoder vector search (cosine similarity) is fast ($O(\log N)$) but evaluates query and document embeddings independently, missing fine-grained keyword interactions. CrossEncoders process query and document together through full transformer attention, delivering significantly higher precision. Using bi-encoders for Stage 1 (candidate retrieval) and CrossEncoders for Stage 2 (top 3 selection) achieves optimal speed and accuracy.

### Q3: How is the architecture kept provider-agnostic for future LLMs?
**Answer**: Through clean interface abstractions (`LLMFactory`, `BaseEmbeddings`, `BaseVectorStore`). The domain agents interact solely with standard interfaces. Swapping from local Ollama / SentenceTransformers to OpenAI GPT-4o or AWS Bedrock Claude 3.5 Sonnet requires zero code modifications—only updating environment variables in `settings.py`.

---

## 7. MLOps, LangGraph & Java Integration Guide
- **LangGraph Integration**: The `AgentOrchestrator` is structured to cleanly transition into a LangGraph `StateGraph` with state persistence and cyclic reflection nodes.
- **Spring Boot Feign Client**: Java microservices consume FastAPI endpoints via Feign Clients or Spring `RestClient` with correlation IDs passed in headers (`X-User-Id`, `X-Correlation-Id`).
