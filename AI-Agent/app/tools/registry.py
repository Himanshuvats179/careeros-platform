import json
from typing import Dict, Any, Callable, List
from app.utils.logger import logger

class ToolRegistry:
    """
    Modular Tool Registry for Multi-Agent Task Execution.
    Provides tools for skill extraction, resume parsing, salary benchmarking, and search mock.
    """

    def __init__(self):
        self._tools: Dict[str, Callable] = {}
        self._register_default_tools()

    def register_tool(self, name: str, func: Callable):
        self._tools[name] = func
        logger.info(f"Registered tool: '{name}'")

    def execute_tool(self, tool_name: str, **kwargs) -> Dict[str, Any]:
        if tool_name not in self._tools:
            logger.warn(f"Tool '{tool_name}' not found in registry.")
            return {"error": f"Tool '{tool_name}' not found"}

        try:
            logger.info(f"Executing tool '{tool_name}' with arguments: {kwargs}")
            return self._tools[tool_name](**kwargs)
        except Exception as e:
            logger.error(f"Error executing tool '{tool_name}': {e}")
            return {"error": str(e)}

    def list_tools(self) -> List[str]:
        return list(self._tools.keys())

    def _register_default_tools(self):
        # 1. Skill Extractor Tool
        def extract_skills(text: str) -> Dict[str, Any]:
            tech_keywords = ["Java", "Spring Boot", "Python", "FastAPI", "PostgreSQL", "Kafka", "Redis", "Docker", "Kubernetes", "AWS", "React", "TypeScript"]
            found = [kw for kw in tech_keywords if kw.lower() in text.lower()]
            return {"extracted_skills": found, "count": len(found)}

        # 2. Resume Parser Tool
        def parse_resume_metadata(text: str) -> Dict[str, Any]:
            lines = [l.strip() for l in text.split("\n") if l.strip()]
            return {
                "word_count": len(text.split()),
                "lines_count": len(lines),
                "has_contact_info": "@" in text or "phone" in text.lower(),
                "sample_header": lines[0] if lines else ""
            }

        # 3. Salary Benchmark Tool
        def get_salary_benchmark(role: str, years_exp: int = 5) -> Dict[str, Any]:
            base = 120000 + (years_exp * 8000)
            return {
                "role": role,
                "years_experience": years_exp,
                "median_salary_usd": base,
                "range_usd": f"${base-15000:,} - ${base+25000:,}"
            }

        self.register_tool("extract_skills", extract_skills)
        self.register_tool("parse_resume_metadata", parse_resume_metadata)
        self.register_tool("get_salary_benchmark", get_salary_benchmark)

tool_registry = ToolRegistry()
