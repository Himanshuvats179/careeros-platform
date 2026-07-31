import os
import uuid
from typing import List, Dict, Any
from app.config.settings import settings
from app.utils.logger import logger

class VectorStoreService:
    """
    RAG Vector Store Service backed by ChromaDB.
    Supports indexing documents (resumes, job requirements, system design topics)
    and performing cosine similarity search for retrieval-augmented generation.
    """

    def __init__(self):
        self.db_dir = settings.CHROMA_DB_DIR
        self.collection_name = settings.VECTOR_COLLECTION_NAME
        self.is_initialized = False
        self.memory_store: List[Dict[str, Any]] = []

        try:
            import chromadb
            self.client = chromadb.PersistentClient(path=self.db_dir)
            self.collection = self.client.get_or_create_collection(name=self.collection_name)
            self.is_initialized = True
            logger.info(f"Chroma Vector DB initialized successfully at {self.db_dir}")
        except Exception as e:
            logger.warn(f"ChromaDB persistent storage fallback to in-memory mode: {e}")
            self.is_initialized = False

    def index_resume(self, user_id: str, resume_text: str, metadata: Optional[Dict[str, Any]] = None) -> str:
        doc_id = f"resume_{user_id}_{uuid.uuid4().hex[:8]}"
        meta = metadata or {"user_id": user_id, "type": "resume"}
        meta["user_id"] = user_id

        if self.is_initialized:
            try:
                self.collection.add(documents=[resume_text], metadatas=[meta], ids=[doc_id])
                logger.info(f"Indexed candidate resume in ChromaDB with ID: {doc_id}")
                return doc_id
            except Exception as e:
                logger.error(f"Failed to add resume to ChromaDB: {e}")

        # In-memory fallback
        self.memory_store.append({"id": doc_id, "content": resume_text, "metadata": meta})
        logger.info(f"Indexed resume in-memory fallback store with ID: {doc_id}")
        return doc_id

    def index_job_posting(self, job_id: str, title: str, description: str, skills: List[str]) -> str:
        doc_id = f"job_{job_id}"
        content = f"Job Title: {title}\nRequired Skills: {', '.join(skills)}\nDescription:\n{description}"
        meta = {"job_id": job_id, "title": title, "type": "job_posting"}

        if self.is_initialized:
            try:
                self.collection.add(documents=[content], metadatas=[meta], ids=[doc_id])
                logger.info(f"Indexed job posting in ChromaDB with ID: {doc_id}")
                return doc_id
            except Exception as e:
                logger.error(f"Failed to add job posting to ChromaDB: {e}")

        self.memory_store.append({"id": doc_id, "content": content, "metadata": meta})
        return doc_id

    def similarity_search(self, query: str, n_results: int = 3) -> List[Dict[str, Any]]:
        if self.is_initialized:
            try:
                results = self.collection.query(query_texts=[query], n_results=n_results)
                documents = results.get("documents", [[]])[0]
                metadatas = results.get("metadatas", [[]])[0]

                retrieved = []
                for doc, meta in zip(documents, metadatas):
                    retrieved.append({"content": doc, "metadata": meta})
                return retrieved
            except Exception as e:
                logger.error(f"ChromaDB search error: {e}")

        # In-memory keyword match search fallback
        query_words = set(query.lower().split())
        scored_docs = []
        for item in self.memory_store:
            content_words = set(item["content"].lower().split())
            overlap = len(query_words.intersection(content_words))
            if overlap > 0:
                scored_docs.append((overlap, item))

        scored_docs.sort(key=lambda x: x[0], reverse=True)
        return [{"content": item[1]["content"], "metadata": item[1]["metadata"]} for item in scored_docs[:n_results]]

vector_store_service = VectorStoreService()
