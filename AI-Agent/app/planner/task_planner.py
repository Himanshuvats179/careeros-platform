from typing import List, Dict, Any
from app.utils.logger import logger

class TaskPlanner:
    """
    Task Decomposition & Goal Reasoning Planner.
    Breaks complex career goals into structured execution steps.
    """

    def plan_task(self, goal: str, context: Dict[str, Any] = None) -> List[Dict[str, Any]]:
        logger.info(f"TaskPlanner: Generating execution plan for goal: '{goal}'")
        goal_lower = goal.lower()

        if "ats" in goal_lower or "score" in goal_lower:
            return [
                {"step": 1, "action": "parse_resume", "tool": "parse_resume_metadata"},
                {"step": 2, "action": "extract_skills", "tool": "extract_skills"},
                {"step": 3, "action": "rag_search", "description": "Search job context in vector store"},
                {"step": 4, "action": "calculate_match", "description": "Compute ATS percentage score"}
            ]
        elif "roadmap" in goal_lower or "career" in goal_lower:
            return [
                {"step": 1, "action": "extract_skills", "tool": "extract_skills"},
                {"step": 2, "action": "salary_lookup", "tool": "get_salary_benchmark"},
                {"step": 3, "action": "generate_roadmap", "description": "Build step-by-step career transition plan"}
            ]

        # General Multi-Step Plan
        return [
            {"step": 1, "action": "extract_skills", "tool": "extract_skills"},
            {"step": 2, "action": "rag_context", "description": "Retrieve grounded RAG context"},
            {"step": 3, "action": "generate_response", "description": "Generate AI response"}
        ]

task_planner = TaskPlanner()
