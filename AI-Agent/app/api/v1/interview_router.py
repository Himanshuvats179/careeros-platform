from fastapi import APIRouter, HTTPException, status
from app.schemas.interview import (
    InterviewQuestionsRequest, InterviewQuestionsResponse,
    MockInterviewRequest, MockInterviewResponse
)
from app.schemas.common import APIResponse
from app.services.ai_service import ai_service

router = APIRouter(prefix="/interview", tags=["Interview AI Agents"])

@router.post("/questions", response_model=APIResponse[InterviewQuestionsResponse])
async def generate_interview_questions(request: InterviewQuestionsRequest):
    try:
        data = await ai_service.generate_interview_questions(
            request.user_id, request.target_role, request.experience_level, request.focus_areas
        )
        return APIResponse(message="Interview questions generated successfully", data=InterviewQuestionsResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.post("/mock-interview", response_model=APIResponse[MockInterviewResponse])
async def evaluate_mock_interview(request: MockInterviewRequest):
    try:
        data = await ai_service.evaluate_mock_interview(request.user_id, request.question, request.user_answer, request.target_role)
        return APIResponse(message="Mock interview answer evaluated successfully", data=MockInterviewResponse(**data))
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))
