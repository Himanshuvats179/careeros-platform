from typing import List, Dict, Any
from app.vector_store.base import BaseVectorStore
from app.embeddings.sentence_transformer import embedding_provider
from app.config.settings import settings
from app.utils.logger import logger

class ChromaVectorStore(BaseVectorStore):
    """
    ChromaDB Vector Store Implementation with dense embedding indexing and memory fallback.
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
            logger.info(f"ChromaDB persistent vector store initialized at {self.db_dir}")
        except Exception as e:
            logger.warn(f"ChromaDB persistent storage offline ({e}). Using in-memory store.")

    def add_texts(self, texts: List[str], metadatas: List[Dict[str, Any]], ids: List[str]) -> bool:
        embeddings = embedding_provider.embed_documents(texts)
        if self.is_initialized:
            try:
                self.collection.add(
                    documents=texts,
                    embeddings=embeddings,
                    metadatas=metadatas,
                    ids=ids
                )
                logger.info(f"Indexed {len(texts)} documents in ChromaDB collection '{self.collection_name}'")
                return True
            except Exception as e:
                logger.error(f"Error adding texts to ChromaDB: {e}")

        # In-memory fallback
        for doc, meta, doc_id, emb in zip(texts, metadatas, ids, embeddings):
            self.memory_store.append({
                "id": doc_id,
                "content": doc,
                "metadata": meta,
                "embedding": emb
            })
        logger.info(f"Indexed {len(texts)} documents in fallback memory vector store.")
        return True

    def search(self, query: str, top_k: int = 5) -> List[Dict[str, Any]]:
        query_embedding = embedding_provider.embed_text(query)
        if self.is_initialized:
            try:
                results = self.collection.query(
                    query_embeddings=[query_embedding],
                    n_results=top_k
                )
                documents = results.get("documents", [[]])[0]
                metadatas = results.get("metadatas", [[]])[0]
                ids = results.get("ids", [[]])[0]

                retrieved = []
                for doc, meta, doc_id in zip(documents, metadatas, ids):
                    retrieved.append({
                        "id": doc_id,
                        "content": doc,
                        "metadata": meta
                    })
                return retrieved
            except Exception as e:
                logger.error(f"ChromaDB query error: {e}")

        # Memory store cosine similarity search
        query_words = set(query.lower().split())
        scored = []
        for item in self.memory_store:
            content_words = set(item["content"].lower().split())
            score = len(query_words.intersection(content_words)) / (len(query_words) or 1)
            scored.append((score, item))

        scored.sort(key=lambda x: x[0], reverse=True)
        return [{"id": item[1]["id"], "content": item[1]["content"], "metadata": item[1]["metadata"]} for item in scored[:top_k]]

chroma_vector_store = ChromaVectorStore()
