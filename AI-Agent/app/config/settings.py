import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    PROJECT_NAME: str = "CareerOS AI Agent Microservice"
    VERSION: str = "1.0.0"
    API_V1_STR: str = "/api/v1"
    
    # LLM Provider Configuration
    LLM_PROVIDER: str = os.getenv("LLM_PROVIDER", "openai").lower()  # openai | bedrock | ollama | mock
    OPENAI_API_KEY: str = os.getenv("OPENAI_API_KEY", "mock-openai-key")
    OPENAI_MODEL: str = os.getenv("OPENAI_MODEL", "gpt-4o")
    USE_AWS_BEDROCK: bool = os.getenv("USE_AWS_BEDROCK", "false").lower() == "true"
    BEDROCK_MODEL_ID: str = os.getenv("BEDROCK_MODEL_ID", "anthropic.claude-3-5-sonnet-20240620-v1:0")
    OLLAMA_BASE_URL: str = os.getenv("OLLAMA_BASE_URL", "http://localhost:11434")
    OLLAMA_MODEL: str = os.getenv("OLLAMA_MODEL", "llama3")
    TEMPERATURE: float = 0.2
    
    # Embeddings & RAG Configuration
    EMBEDDING_PROVIDER: str = os.getenv("EMBEDDING_PROVIDER", "sentence-transformers")  # sentence-transformers | openai
    EMBEDDING_MODEL_NAME: str = os.getenv("EMBEDDING_MODEL_NAME", "all-MiniLM-L6-v2")
    RERANKER_ENABLED: bool = os.getenv("RERANKER_ENABLED", "true").lower() == "true"
    RERANKER_MODEL_NAME: str = os.getenv("RERANKER_MODEL_NAME", "cross-encoder/ms-marco-MiniLM-L-6-v2")
    
    # Vector Database Settings
    VECTOR_STORE_PROVIDER: str = os.getenv("VECTOR_STORE_PROVIDER", "chroma")  # chroma | faiss | memory
    CHROMA_DB_DIR: str = os.getenv("CHROMA_DB_DIR", "./chroma_db")
    VECTOR_COLLECTION_NAME: str = "careeros_knowledge_base"
    
    # Evaluation & Guardrails
    GUARDRAIL_CONFIDENCE_THRESHOLD: float = float(os.getenv("GUARDRAIL_CONFIDENCE_THRESHOLD", "0.70"))
    
    # AWS S3 / Cloud Storage Settings
    USE_AWS_S3: bool = os.getenv("USE_AWS_S3", "false").lower() == "true"
    AWS_ACCESS_KEY_ID: str = os.getenv("AWS_ACCESS_KEY_ID", "")
    AWS_SECRET_ACCESS_KEY: str = os.getenv("AWS_SECRET_ACCESS_KEY", "")
    AWS_REGION: str = os.getenv("AWS_REGION", "us-east-1")
    AWS_S3_BUCKET_NAME: str = os.getenv("AWS_S3_BUCKET_NAME", "careeros-ai-artifacts")
    LOCAL_UPLOAD_DIR: str = "./uploads"

    # Kafka Integration Settings
    KAFKA_BOOTSTRAP_SERVERS: str = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092")
    KAFKA_AUDIT_TOPIC: str = "careeros.ai.events"

    class Config:
        case_sensitive = True

settings = Settings()
