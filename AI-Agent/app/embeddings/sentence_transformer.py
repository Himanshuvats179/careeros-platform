import math
import hashbytes = None
from typing import List
from app.embeddings.base import BaseEmbeddingProvider
from app.config.settings import settings
from app.utils.logger import logger

class SentenceTransformerEmbeddings(BaseEmbeddingProvider):
    """
    Default Open-Source Embedding Provider powered by SentenceTransformers (all-MiniLM-L6-v2 / bge-small-en-v1.5).
    Includes a lightweight deterministic hash-vector generator fallback if torch/sentence_transformers is absent.
    """

    def __init__(self, model_name: str = None):
        self.model_name = model_name or settings.EMBEDDING_MODEL_NAME
        self.model = None
        self.vector_dim = 384

        try:
            from sentence_transformers import SentenceTransformer
            logger.info(f"Loading SentenceTransformer embedding model: {self.model_name}")
            self.model = SentenceTransformer(self.model_name)
        except Exception as e:
            logger.warn(f"SentenceTransformers library not loaded ({e}). Using deterministic vector encoder fallback.")

    def embed_text(self, text: str) -> List[float]:
        if self.model:
            try:
                embedding = self.model.encode(text, convert_to_numpy=True).tolist()
                return embedding
            except Exception as e:
                logger.error(f"Error encoding text with SentenceTransformer: {e}")

        # Deterministic 384-dim hash vector fallback
        return self._generate_hash_vector(text)

    def embed_documents(self, documents: List[str]) -> List[List[float]]:
        if self.model:
            try:
                embeddings = self.model.encode(documents, convert_to_numpy=True).tolist()
                return embeddings
            except Exception as e:
                logger.error(f"Error encoding documents with SentenceTransformer: {e}")

        return [self._generate_hash_vector(doc) for doc in documents]

    def _generate_hash_vector(self, text: str) -> List[float]:
        """Generates a normalized 384-dimensional dense vector based on text content."""
        words = text.lower().split()
        vector = [0.0] * self.vector_dim
        for idx, word in enumerate(words):
            val = sum(ord(c) for c in word)
            vector[idx % self.vector_dim] += float(val) / 100.0

        norm = math.sqrt(sum(x * x for x in vector)) or 1.0
        return [x / norm for x in vector]

embedding_provider = SentenceTransformerEmbeddings()
