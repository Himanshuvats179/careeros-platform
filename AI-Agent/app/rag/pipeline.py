import uuid
from typing import List, Dict, Any
from app.vector_store.chroma_store import chroma_vector_store
from app.ml.reranker import reranker
from app.utils.logger import logger

class RAGPipeline:
    """
    2-Stage Production Retrieval-Augmented Generation (RAG) Pipeline:
    - Stage 1: Document Ingestion, Chunking & Vector Cosine Similarity Search
    - Stage 2: CrossEncoder Semantic Reranking & Context Assembly
    """

    def __init__(self):
        self.vector_store = chroma_vector_store
        self.reranker = reranker

    def chunk_text(self, text: str, chunk_size: int = 500, overlap: int = 50) -> List[str]:
        """Recursive character text splitter."""
        if len(text) <= chunk_size:
            return [text]

        chunks = []
        start = 0
        while start < len(text):
            end = start + chunk_size
            chunk = text[start:end]
            chunks.append(chunk.strip())
            start += chunk_size - overlap
        return [c for c in chunks if c]

    def ingest_document(self, user_id: str, document_text: str, doc_type: str = "resume", metadata: Dict[str, Any] = None) -> List[str]:
        """Ingests a document into the RAG vector store with recursive chunking."""
        chunks = self.chunk_text(document_text)
        ids = [f"{doc_type}_{user_id}_{uuid.uuid4().hex[:8]}" for _ in chunks]
        metadatas = []

        for idx, chunk in enumerate(chunks):
            meta = metadata.copy() if metadata else {}
            meta.update({
                "user_id": user_id,
                "doc_type": doc_type,
                "chunk_index": idx,
                "total_chunks": len(chunks)
            })
            metadatas.append(meta)

        self.vector_store.add_texts(texts=chunks, metadatas=metadatas, ids=ids)
        logger.info(f"RAGPipeline: Ingested document type '{doc_type}' into {len(chunks)} chunks for user {user_id}")
        return ids

    def retrieve_and_rerank(self, query: str, top_k_retrieval: int = 10, top_n_rerank: int = 3) -> List[Dict[str, Any]]:
        """Executes 2-stage retrieval: Stage 1 Vector Search -> Stage 2 CrossEncoder Rerank."""
        logger.info(f"RAGPipeline: Executing Stage-1 vector search for query: '{query}'")
        candidates = self.vector_store.search(query=query, top_k=top_k_retrieval)

        if not candidates:
            return []

        logger.info(f"RAGPipeline: Executing Stage-2 CrossEncoder reranking on {len(candidates)} candidate chunks")
        reranked = self.reranker.rerank(query=query, candidate_docs=candidates, top_n=top_n_rerank)
        return reranked

    def assemble_context_prompt(self, query: str, top_chunks: List[Dict[str, Any]]) -> str:
        """Assembles reranked retrieved context blocks into a grounded LLM prompt."""
        if not top_chunks:
            return f"User Query: {query}\n\nNo relevant background knowledge retrieved."

        context_str = "\n---\n".join([f"[Context Block {idx+1}]:\n{chunk['content']}" for idx, chunk in enumerate(top_chunks)])
        prompt = (
            f"Use the following retrieved context blocks to answer the user query accurately.\n"
            f"Ground your response strictly in the provided context.\n\n"
            f"RETRIEVED CONTEXT:\n{context_str}\n\n"
            f"USER QUERY: {query}"
        )
        return prompt

rag_pipeline = RAGPipeline()
