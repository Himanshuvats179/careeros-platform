from typing import List, Dict, Any
from app.config.settings import settings
from app.utils.logger import logger

class CrossEncoderReranker:
    """
    Stage-2 CrossEncoder Semantic Reranker.
    Reranks top-K retrieved candidate document chunks by jointly processing (query, chunk) pairs.
    Uses sentence_transformers CrossEncoder (ms-marco-MiniLM-L-6-v2) or word-level TF-IDF overlap scoring.
    """

    def __init__(self, model_name: str = None):
        self.model_name = model_name or settings.RERANKER_MODEL_NAME
        self.model = None

        if settings.RERANKER_ENABLED:
            try:
                from sentence_transformers import CrossEncoder
                logger.info(f"Loading CrossEncoder reranker model: {self.model_name}")
                self.model = CrossEncoder(self.model_name)
            except Exception as e:
                logger.warn(f"CrossEncoder library not loaded ({e}). Using heuristic TF-IDF reranker.")

    def rerank(self, query: str, candidate_docs: List[Dict[str, Any]], top_n: int = 3) -> List[Dict[str, Any]]:
        if not candidate_docs:
            return []

        if self.model:
            try:
                pairs = [[query, doc["content"]] for doc in candidate_docs]
                scores = self.model.predict(pairs)
                for doc, score in zip(candidate_docs, scores):
                    doc["rerank_score"] = float(score)

                sorted_docs = sorted(candidate_docs, key=lambda x: x["rerank_score"], reverse=True)
                logger.info(f"CrossEncoder reranked {len(candidate_docs)} candidates down to top {top_n}")
                return sorted_docs[:top_n]
            except Exception as e:
                logger.error(f"Error during CrossEncoder reranking: {e}")

        # Heuristic word overlap reranking fallback
        query_words = set(query.lower().split())
        for doc in candidate_docs:
            doc_words = set(doc["content"].lower().split())
            intersection = query_words.intersection(doc_words)
            doc["rerank_score"] = len(intersection) / (len(query_words) or 1)

        sorted_docs = sorted(candidate_docs, key=lambda x: x["rerank_score"], reverse=True)
        return sorted_docs[:top_n]

reranker = CrossEncoderReranker()
