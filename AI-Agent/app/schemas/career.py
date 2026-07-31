from typing import List, Optional
from pydantic import BaseModel, Field

class CareerRoadmapRequest(BaseModel):
    user_id: str
    current_role: str
    target_role: str
    years_of_experience: int
    current_skills: List[str]

class Milestone(BaseModel):
    phase: str
    timeframe: str
    goals: List[str]
    recommended_skills: List[str]

class CareerRoadmapResponse(BaseModel):
    target_role: str
    estimated_timeline_months: int
    milestones: List[Milestone]
    summary: str

class SkillGapRequest(BaseModel):
    user_id: str
    current_skills: List[str]
    target_role: str

class SkillGapResponse(BaseModel):
    target_role: str
    possessed_skills: List[str]
    missing_critical_skills: List[str]
    missing_nice_to_have_skills: List[str]
    match_percentage: int

class LearningPathRequest(BaseModel):
    user_id: str
    skill_to_learn: str
    preferred_level: str = "INTERMEDIATE"

class ResourceRecommendation(BaseModel):
    title: str
    type: str  # Course, Book, Documentation, Project
    url: Optional[str] = None
    estimated_hours: int

class LearningPathResponse(BaseModel):
    skill: str
    overview: str
    resources: List[ResourceRecommendation]
    hands_on_project_idea: str
