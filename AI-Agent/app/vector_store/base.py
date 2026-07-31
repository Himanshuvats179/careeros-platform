from abc import ABC, abstractmethod
from typing import List, Dict, Any

class BaseVectorStore(ABC):
    """
    Abstract Vector Store Interface.
    Enables ChromaDB, FAISS, OpenSearch, or In-Memory vector indexing engines.
    """

    @abstractmethod
    def add_texts(self, texts: List[str], metadatas: List[Dict[str, Any]], ids: List[str]) -> bool:
        """Add text chunks and metadata into the vector index."""
        pass

    @abstractmethod
    def search(self, query: str, top_k: int = 5) -> List[Dict[str, Any]]:
        """Perform dense vector similarity search returning top-k matching documents."""
        pass
