from pydantic import BaseModel, Field
from typing import List, Dict, Any, Optional

class IngestDocumentRequest(BaseModel):
    user_id: str = Field(..., example="user_12345")
    document_text: str = Field(..., example="Senior Software Engineer with 7+ years experience in Java 21, Spring Boot 3, Kafka, and Redis.")
    doc_type: str = Field("resume", example="resume")
    metadata: Optional[Dict[str, Any]] = None

class IngestDocumentResponse(BaseModel):
    status: str = Field("success", example="success")
    document_ids: List[str]
    chunk_count: int

class SearchRAGRequest(BaseModel):
    query: str = Field(..., example="What experience does candidate have with Kafka and Spring Boot?")
    top_k: int = Field(5, example=5)
    top_n_rerank: int = Field(3, example=3)

class SearchRAGResponse(BaseModel):
    query: str
    retrieved_count: int
    results: List[Dict[str, Any]]

class QueryRAGRequest(BaseModel):
    user_id: str = Field(..., example="user_12345")
    query: str = Field(..., example="Summarize candidate's system design and backend architecture experience.")

class QueryRAGResponse(BaseModel):
    query: str
    answer: str
    rag_sources: List[Dict[str, Any]]
    confidence_score: float
