import json
from typing import List, Optional, Dict, Any
from app.agents.resume_agent import resume_agent
from app.agents.career_agent import career_agent
from app.agents.interview_agent import interview_agent
from app.services.vector_store_service import vector_store_service
from app.services.kafka_producer import ai_event_producer
from app.services.llm_factory import llm_factory
from app.prompts.templates import COVER_LETTER_PROMPT
from app.config.settings import settings
from app.utils.logger import logger

class AIService:
    """
    Core AI Orchestration Service linking Multi-Agent LLM Providers,
    ChromaDB RAG Vector Store Retrieval, and Kafka Event Streaming.
    """

    async def analyze_resume(self, user_id: str, resume_text: str, target_role: str = "Software Engineer") -> Dict[str, Any]:
        logger.info(f"AIService: Analyzing resume for user '{user_id}' target role '{target_role}'")
        # Index resume in RAG Vector Store
        vector_store_service.index_resume(user_id=user_id, resume_text=resume_text)

        result = await resume_agent.analyze_resume(resume_text, target_role)

        # Publish Kafka AI Event
        ai_event_producer.publish_ai_event(
            event_type="RESUME_ANALYZED",
            user_id=user_id,
            payload={"target_role": target_role, "summary": result.get("summary")}
        )
        return result

    async def improve_resume(self, user_id: str, resume_text: str, job_description: str = "") -> Dict[str, Any]:
        result = await resume_agent.improve_resume(resume_text, job_description)
        ai_event_producer.publish_ai_event(
            event_type="RESUME_IMPROVED",
            user_id=user_id,
            payload={"overall_impact_score": result.get("overall_impact_score")}
        )
        return result

    async def calculate_ats_score(self, user_id: str, resume_text: str, job_description: str) -> Dict[str, Any]:
        # RAG context enrichment
        rag_matches = vector_store_service.similarity_search(query=job_description, n_results=2)
        logger.info(f"RAG ATS Search: retrieved {len(rag_matches)} vector context matches")

        result = await resume_agent.calculate_ats_score(resume_text, job_description)
        result["rag_context_retrieved"] = len(rag_matches)

        ai_event_producer.publish_ai_event(
            event_type="ATS_SCORE_CALCULATED",
            user_id=user_id,
            payload={"match_percentage": result.get("match_percentage")}
        )
        return result

    async def generate_career_roadmap(self, user_id: str, current_role: str, target_role: str, experience_years: int, current_skills: List[str]) -> Dict[str, Any]:
        result = await career_agent.generate_career_roadmap(current_role, target_role, experience_years, current_skills)
        ai_event_producer.publish_ai_event(
            event_type="CAREER_ROADMAP_GENERATED",
            user_id=user_id,
            payload={"target_role": target_role, "steps_count": len(result.get("roadmap_steps", []))}
        )
        return result

    async def generate_interview_questions(self, user_id: str, target_role: str, experience_level: str, focus_areas: List[str]) -> Dict[str, Any]:
        return await interview_agent.generate_questions(target_role, experience_level, focus_areas)

    async def evaluate_mock_interview(self, user_id: str, question: str, user_answer: str, target_role: str) -> Dict[str, Any]:
        result = await interview_agent.evaluate_mock_interview(question, user_answer, target_role)
        ai_event_producer.publish_ai_event(
            event_type="INTERVIEW_EVALUATED",
            user_id=user_id,
            payload={"score": result.get("overall_score")}
        )
        return result

    async def generate_cover_letter(self, user_id: str, user_name: str, target_company: str, target_role: str, resume_summary: str, job_description: str) -> Dict[str, Any]:
        logger.info(f"AIService: Generating Cover Letter for '{user_name}' -> '{target_company}'")
        if settings.OPENAI_API_KEY == "mock-openai-key" and not settings.USE_AWS_BEDROCK:
            return {
                "cover_letter_text": f"Dear Hiring Team at {target_company},\n\nI am writing to express my enthusiastic interest in the {target_role} position. With my background in high-throughput Java microservices, Kafka event streaming, and AI integrations, I am confident in making an immediate impact.\n\nSincerely,\n{user_name}",
                "key_selling_points": [
                    "Experience building distributed event-driven systems",
                    "Expertise in Spring Boot 3, Kafka, Redis, and FastAPI AI agents",
                    "Proven track record of optimizing database latency"
                ]
            }
        prompt = COVER_LETTER_PROMPT.format(
            user_name=user_name,
            target_role=target_role,
            target_company=target_company,
            resume_summary=resume_summary,
            job_description=job_description
        )
        return self._call_llm_json(prompt)

    async def analyze_skill_gap(self, user_id: str, current_skills: List[str], target_role: str) -> Dict[str, Any]:
        return await career_agent.analyze_skill_gap(current_skills, target_role)

    async def generate_learning_path(self, user_id: str, skill_to_learn: str, preferred_level: str = "INTERMEDIATE") -> Dict[str, Any]:
        return await career_agent.generate_learning_path(skill_to_learn, preferred_level)

    async def process_chat(self, user_id: str, session_id: str, message: str, history: Optional[List] = None) -> Dict[str, Any]:
        logger.info(f"AIService: Processing RAG-augmented Chat for session {session_id}")
        rag_context = vector_store_service.similarity_search(query=message, n_results=2)
        context_snippets = [item["content"][:200] for item in rag_context]

        reply_prefix = f"Based on your profile context ({len(rag_context)} documents referenced): " if context_snippets else ""

        return {
            "session_id": session_id,
            "reply": f"{reply_prefix}To stand out for roles matching '{message}', focus on demonstrating event-driven design, system performance metrics, and clean microservice architecture.",
            "rag_sources": context_snippets,
            "suggested_followups": [
                "How do I optimize my resume for ATS screeners?",
                "Can you generate mock interview questions for System Design?",
                "What skills am I missing for a Principal Staff Engineer role?"
            ]
        }

    def _call_llm_json(self, prompt: str) -> Dict[str, Any]:
        try:
            llm = llm_factory.get_llm()
            res = llm.invoke(prompt)
            return json.loads(res.content)
        except Exception as e:
            logger.error(f"LLM invocation error: {e}")
            raise RuntimeError(f"Failed to generate LLM response: {e}")

ai_service = AIService()
