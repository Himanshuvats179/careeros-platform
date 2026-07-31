import math
from typing import Dict, Any, Optional, List
from app.embeddings.sentence_transformer import embedding_provider
from app.utils.logger import logger

class SemanticCache:
    """
    Enterprise Semantic Caching Layer using Vector Cosine Similarity.
    Prevents redundant LLM API invocations by caching query-response pairs.
    Returns cached response if query similarity >= similarity_threshold (default: 0.95).
    """

    def __init__(self, similarity_threshold: float = 0.95):
        self.threshold = similarity_threshold
        self._cache: List[Dict[str, Any]] = []

    def get(self, query: str) -> Optional[Dict[str, Any]]:
        if not self._cache:
            return None

        query_embedding = embedding_provider.embed_text(query)
        best_score = -1.0
        best_entry = None

        for entry in self._cache:
            score = self._cosine_similarity(query_embedding, entry["embedding"])
            if score > best_score:
                best_score = score
                best_entry = entry

        if best_score >= self.threshold and best_entry:
            logger.info(f"SemanticCache: CACHE HIT (Similarity: {best_score:.4f} >= {self.threshold}) for query: '{query}'")
            cached_data = best_entry["response"].copy()
            cached_data["_cache_hit"] = True
            cached_data["_similarity_score"] = round(best_score, 4)
            return cached_data

        logger.info(f"SemanticCache: CACHE MISS (Best similarity: {best_score:.4f} < {self.threshold})")
        return None

    def set(self, query: str, response: Dict[str, Any]):
        query_embedding = embedding_provider.embed_text(query)
        self._cache.append({
            "query": query,
            "embedding": query_embedding,
            "response": response
        })
        logger.info(f"SemanticCache: Stored new query response pair. Total cache size: {len(self._cache)}")

    def _cosine_similarity(self, vec_a: List[float], vec_b: List[float]) -> float:
        dot_product = sum(a * b for a, b in zip(vec_a, vec_b))
        norm_a = math.sqrt(sum(a * a for a in vec_a)) or 1.0
        norm_b = math.sqrt(sum(b * b for b in vec_b)) or 1.0
        return dot_product / (norm_a * norm_b)

semantic_cache = SemanticCache()
