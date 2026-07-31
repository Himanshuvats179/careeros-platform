import json
from app.config.settings import settings
from app.prompts.templates import RESUME_ANALYZE_PROMPT, RESUME_IMPROVE_PROMPT, ATS_SCORE_PROMPT
from app.utils.logger import logger

class ResumeAgent:
    def __init__(self):
        self.model = settings.OPENAI_MODEL

    async def analyze_resume(self, resume_text: str, target_role: str = "Software Engineer") -> dict:
        logger.info(f"ResumeAgent: Analyzing resume for role '{target_role}'")
        if settings.OPENAI_API_KEY == "mock-openai-key":
            return {
                "summary": f"Strong engineering candidate profile targeted for {target_role} with solid backend capabilities.",
                "strengths": [
                    "Demonstrated expertise in Java 21, Spring Boot 3, and microservices architecture.",
                    "Experience with message brokers (Kafka, RabbitMQ) and caching (Redis).",
                    "Strong database migration practices using Flyway."
                ],
                "weaknesses": [
                    "Resume lacks explicit metrics on latency reductions and throughput improvements.",
                    "Could highlight more cloud deployment experience (AWS / Docker / Kubernetes)."
                ],
                "detected_skills": [
                    {"category": "Backend", "skills": ["Java 21", "Spring Boot 3", "PostgreSQL"]},
                    {"category": "DevOps/Infra", "skills": ["Docker", "Redis", "Kafka", "Flyway"]}
                ],
                "formatting_score": 88
            }

        prompt = RESUME_ANALYZE_PROMPT.format(resume_text=resume_text, target_role=target_role)
        # Call OpenAI LLM via LangChain / OpenAI Client
        return self._call_llm_json(prompt)

    async def improve_resume(self, resume_text: str, job_description: str = "") -> dict:
        logger.info("ResumeAgent: Improving resume bullet points & impact metrics")
        if settings.OPENAI_API_KEY == "mock-openai-key":
            return {
                "improved_resume_text": f"# Optimized Resume for Target Position\n\n## Professional Summary\nResult-driven Senior Software Engineer specializing in scalable microservices and AI-driven platforms.\n\n## Work Experience\n- **Staff Software Engineer**: Architected high-throughput microservices platform reducing API latency by 45% using Redis caching and PostgreSQL indexing.\n- **Backend Lead**: Implemented event-driven audit log system with Apache Kafka processing 50,000 events/second with zero data loss.",
                "action_items": [
                    {
                        "section": "Experience",
                        "original": "Worked on backend APIs with Spring Boot",
                        "improved": "Architected high-throughput microservices platform reducing API latency by 45% using Redis caching",
                        "rationale": "Incorporated quantifiable metrics and modern stack details"
                    }
                ],
                "overall_impact_score": 94
            }
        prompt = RESUME_IMPROVE_PROMPT.format(resume_text=resume_text, job_description=job_description)
        return self._call_llm_json(prompt)

    async def calculate_ats_score(self, resume_text: str, job_description: str) -> dict:
        logger.info("ResumeAgent: Calculating ATS score against job description")
        if settings.OPENAI_API_KEY == "mock-openai-key":
            return {
                "match_percentage": 86,
                "matched_keywords": ["Java 21", "Spring Boot", "Kafka", "PostgreSQL", "Microservices", "Docker"],
                "missing_keywords": ["Kubernetes", "AWS EKS", "Terraform"],
                "formatting_issues": ["Single-column clean markdown layout detected (Optimal for ATS)"],
                "recommendations": [
                    "Include Kubernetes and cloud infrastructure automation keywords.",
                    "Highlight distributed locking experience in experience section."
                ]
            }
        prompt = ATS_SCORE_PROMPT.format(resume_text=resume_text, job_description=job_description)
        return self._call_llm_json(prompt)

    def _call_llm_json(self, prompt: str) -> dict:
        try:
            from langchain_openai import ChatOpenAI
            llm = ChatOpenAI(api_key=settings.OPENAI_API_KEY, model=self.model, temperature=0.2)
            res = llm.invoke(prompt)
            return json.loads(res.content)
        except Exception as e:
            logger.error(f"OpenAI invocation error: {e}")
            raise RuntimeError(f"Failed to generate LLM response: {e}")

resume_agent = ResumeAgent()
