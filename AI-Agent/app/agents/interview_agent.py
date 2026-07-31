import json
from typing import List
from app.config.settings import settings
from app.prompts.templates import INTERVIEW_QUESTIONS_PROMPT
from app.utils.logger import logger

class InterviewAgent:
    def __init__(self):
        self.model = settings.OPENAI_MODEL

    async def generate_questions(self, target_role: str, experience_level: str, focus_areas: List[str]) -> dict:
        logger.info(f"InterviewAgent: Generating questions for '{target_role}'")
        if settings.OPENAI_API_KEY == "mock-openai-key":
            return {
                "target_role": target_role,
                "questions": [
                    {
                        "id": 1,
                        "category": "System Design",
                        "question": "How do you ensure idempotency in a Kafka consumer service storing audit logs in PostgreSQL?",
                        "expected_answer_keypoints": [
                            "Use unique eventId deduplication in DB or Redis",
                            "Use transactional boundary around DB write and offset commit",
                            "Implement Dead Letter Queue (DLQ) for unrecoverable failures"
                        ],
                        "difficulty": "HARD"
                    },
                    {
                        "id": 2,
                        "category": "Backend Architecture",
                        "question": "Explain the difference between Optimistic Locking (@Version) and Pessimistic Locking in Spring Data JPA.",
                        "expected_answer_keypoints": [
                            "Optimistic locking verifies version field during SQL update without holding DB locks",
                            "Pessimistic locking executes SELECT ... FOR UPDATE holding DB row locks",
                            "Optimistic locking minimizes DB thread contention in high-read systems"
                        ],
                        "difficulty": "MEDIUM"
                    },
                    {
                        "id": 3,
                        "category": "Microservices",
                        "question": "How does Spring Cloud API Gateway validate JWT tokens and propagate correlation IDs across downstream microservices?",
                        "expected_answer_keypoints": [
                            "Global WebFilter intercepts incoming HTTP requests",
                            "Decodes and validates JWT claims",
                            "Appends X-User-Id and X-Correlation-Id headers before routing"
                        ],
                        "difficulty": "MEDIUM"
                    }
                ]
            }

        prompt = INTERVIEW_QUESTIONS_PROMPT.format(
            target_role=target_role,
            focus_areas=", ".join(focus_areas)
        )
        return self._call_llm_json(prompt)

    async def evaluate_mock_interview(self, question: str, user_answer: str, target_role: str) -> dict:
        logger.info("InterviewAgent: Evaluating mock interview answer")
        return {
            "question": question,
            "user_answer": user_answer,
            "score_out_of_10": 9,
            "strengths": [
                "Accurately explained event ID deduplication for Kafka idempotency.",
                "Correctly highlighted the trade-offs of optimistic locking in microservices."
            ],
            "improvements": [
                "Consider mentioning Dead Letter Queue (DLQ) automated retry strategies for edge cases."
            ],
            "ideal_sample_answer": "In production systems, idempotency is achieved by extracting the unique eventId from the Kafka message and verifying its existence in a PostgreSQL table or Redis cache before executing business logic. Unrecoverable failures are routed to a .DLQ topic after 3 backoff retries."
        }

    def _call_llm_json(self, prompt: str) -> dict:
        try:
            from langchain_openai import ChatOpenAI
            llm = ChatOpenAI(api_key=settings.OPENAI_API_KEY, model=self.model, temperature=0.2)
            res = llm.invoke(prompt)
            return json.loads(res.content)
        except Exception as e:
            logger.error(f"OpenAI invocation error: {e}")
            raise RuntimeError(f"Failed to generate LLM response: {e}")

interview_agent = InterviewAgent()
