from abc import ABC, abstractmethod
from typing import List

class BaseEmbeddingProvider(ABC):
    """
    Abstract Interface for Embedding Providers.
    Allows SentenceTransformers, OpenAI, or Bedrock Embeddings to be plugged in interchangeably.
    """

    @abstractmethod
    def embed_text(self, text: str) -> List[float]:
        """Embed a single text string into a dense vector representation."""
        pass

    @abstractmethod
    def embed_documents(self, documents: List[str]) -> List[List[float]]:
        """Embed a batch of text documents into dense vector representations."""
        pass
