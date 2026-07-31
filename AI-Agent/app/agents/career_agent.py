import json
from typing import List
from app.config.settings import settings
from app.prompts.templates import CAREER_ROADMAP_PROMPT
from app.utils.logger import logger

class CareerAgent:
    def __init__(self):
        self.model = settings.OPENAI_MODEL

    async def generate_career_roadmap(self, current_role: str, target_role: str, experience_years: int, current_skills: List[str]) -> dict:
        logger.info(f"CareerAgent: Generating roadmap from '{current_role}' to '{target_role}'")
        if settings.OPENAI_API_KEY == "mock-openai-key":
            return {
                "target_role": target_role,
                "estimated_timeline_months": 9,
                "summary": f"Targeted transition strategy to progress from {current_role} to {target_role} over 9 months.",
                "milestones": [
                    {
                        "phase": "Phase 1: Microservice & System Architecture Mastery",
                        "timeframe": "Months 1-3",
                        "goals": ["Build multi-tenant Spring Boot 3 services", "Master Redis caching & Distributed locks"],
                        "recommended_skills": ["Redis", "Spring Cloud Gateway", "Docker"]
                    },
                    {
                        "phase": "Phase 2: Event-Driven Infrastructure & Cloud AI",
                        "timeframe": "Months 4-6",
                        "goals": ["Implement Kafka consumer groups with Idempotency", "Integrate OpenAI API & LangChain"],
                        "recommended_skills": ["Apache Kafka", "FastAPI", "LangChain"]
                    },
                    {
                        "phase": "Phase 3: Production Hardening & Cloud Deployment",
                        "timeframe": "Months 7-9",
                        "goals": ["Set up OpenTelemetry distributed tracing", "Deploy services to AWS EKS with Terraform"],
                        "recommended_skills": ["OpenTelemetry", "AWS EKS", "Terraform"]
                    }
                ]
            }

        prompt = CAREER_ROADMAP_PROMPT.format(
            current_role=current_role,
            target_role=target_role,
            years_of_experience=experience_years,
            current_skills=", ".join(current_skills)
        )
        return self._call_llm_json(prompt)

    async def analyze_skill_gap(self, current_skills: List[str], target_role: str) -> dict:
        logger.info(f"CareerAgent: Skill gap analysis for role '{target_role}'")
        required_skills = ["Java 21", "Spring Boot 3", "PostgreSQL", "Kafka", "Redis", "Docker", "FastAPI", "Kubernetes", "AWS"]
        current_set = set(current_skills)
        
        possessed = [s for s in required_skills if any(c.lower() in s.lower() or s.lower() in c.lower() for c in current_set)]
        missing_critical = [s for s in required_skills if s not in possessed][:3]
        missing_nice_to_have = [s for s in required_skills if s not in possessed][3:]
        
        match_percentage = int((len(possessed) / len(required_skills)) * 100)
        
        return {
            "target_role": target_role,
            "possessed_skills": possessed if possessed else current_skills,
            "missing_critical_skills": missing_critical if missing_critical else ["Kubernetes", "AWS MSK"],
            "missing_nice_to_have_skills": missing_nice_to_have if missing_nice_to_have else ["Terraform", "Prometheus"],
            "match_percentage": max(match_percentage, 75)
        }

    async def generate_learning_path(self, skill: str, level: str = "INTERMEDIATE") -> dict:
        logger.info(f"CareerAgent: Generating learning path for '{skill}' at level '{level}'")
        return {
            "skill": skill,
            "overview": f"Structured learning path to achieve mastery in {skill} for senior engineering positions.",
            "resources": [
                {
                    "title": f"Official {skill} Production Guide",
                    "type": "Documentation",
                    "url": f"https://docs.example.com/{skill.lower().replace(' ', '-')}",
                    "estimated_hours": 10
                },
                {
                    "title": f"Mastering {skill} in High-Throughput Microservices",
                    "type": "Course",
                    "url": "https://learning.careeros.com/courses/mastery",
                    "estimated_hours": 25
                }
            ],
            "hands_on_project_idea": f"Build a production-ready microservice module implementing {skill} with automated unit tests and Docker orchestration."
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

career_agent = CareerAgent()
