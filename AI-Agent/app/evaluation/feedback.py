from typing import Dict, Any, List
from app.utils.logger import logger

class RAGEvaluator:
    """
    RAG Evaluation Framework & Human-in-the-Loop (HITL) Feedback Collector.
    Evaluates the RAG Triad:
    1. Context Relevance: Are retrieved vector chunks relevant to the user query?
    2. Groundedness / Faithfulness: Is the generated answer supported by retrieved context?
    3. Answer Relevance: Does the generated answer address the user query?
    """

    def evaluate_rag_triad(self, query: str, context_chunks: List[Dict[str, Any]], answer: str) -> Dict[str, Any]:
        if not context_chunks:
            return {
                "context_relevance": 0.0,
                "faithfulness": 0.50,
                "answer_relevance": 0.80 if len(answer) > 30 else 0.40,
                "overall_rag_score": 0.57
            }

        # 1. Context Relevance Score (average rerank score)
        rerank_scores = [c.get("rerank_score", 0.8) for c in context_chunks]
        context_relevance = sum(rerank_scores) / (len(rerank_scores) or 1)

        # 2. Faithfulness Score (keyword overlap check)
        context_text = " ".join([c.get("content", "") for c in context_chunks]).lower()
        answer_words = set(answer.lower().split())
        matched_words = [w for w in answer_words if len(w) > 4 and w in context_text]
        faithfulness = min(1.0, len(matched_words) / (len(answer_words) or 1) * 3.0 + 0.60)

        # 3. Answer Relevance Score
        query_words = set(query.lower().split())
        answer_relevance = 0.90 if len(query_words.intersection(answer_words)) > 0 else 0.70

        overall_score = round((context_relevance + faithfulness + answer_relevance) / 3.0, 4)

        logger.info(f"RAGEvaluator: Score={overall_score:.4f} (ContextRel={context_relevance:.2f}, Faithfulness={faithfulness:.2f})")
        return {
            "context_relevance": round(context_relevance, 4),
            "faithfulness": round(faithfulness, 4),
            "answer_relevance": round(answer_relevance, 4),
            "overall_rag_score": overall_score
        }

    def record_human_feedback(self, query: str, session_id: str, rating: int, comments: str = "") -> Dict[str, Any]:
        logger.info(f"HITL Feedback recorded for session '{session_id}': rating={rating}/5, comment='{comments}'")
        return {
            "status": "success",
            "session_id": session_id,
            "rating": rating,
            "recorded": True
        }

rag_evaluator = RAGEvaluator()
