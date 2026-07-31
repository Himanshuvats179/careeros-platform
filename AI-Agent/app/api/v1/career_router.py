from fastapi import APIRouter, HTTPException, status
from app.schemas.career import (
    CareerRoadmapRequest, CareerRoadmapResponse,
    SkillGapRequest, SkillGapResponse,
    LearningPathRequest, LearningPathResponse
)
from app.schemas.common import APIResponse
from app.services.ai_service import ai_service

router = APIRouter(prefix="/career", tags=["Career AI Agents"])

@router.post("/career-roadmap", response_model=APIResponse[CareerRoadmapResponse])
async def generate_career_roadmap(request: CareerRoadmapRequest):
    try:
        data = await ai_service.generate_career_roadmap(
            request.user_id, request.current_role, request.target_role,
            request.years_of_experience, request.current_skills
        )
        return APIResponse(message="Career roadmap generated successfully", data=CareerRoadmapResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/skill-gap", response_model=APIResponse[SkillGapResponse])
async def analyze_skill_gap(request: SkillGapRequest):
    try:
        data = await ai_service.analyze_skill_gap(request.user_id, request.current_skills, request.target_role)
        return APIResponse(message="Skill gap analysis completed", data=SkillGapResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/learning-path", response_model=APIResponse[LearningPathResponse])
async def generate_learning_path(request: LearningPathRequest):
    try:
        data = await ai_service.generate_learning_path(request.user_id, request.skill_to_learn, request.preferred_level)
        return APIResponse(message="Learning path generated successfully", data=LearningPathResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))
