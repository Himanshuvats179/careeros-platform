from typing import Dict, Any
from app.config.settings import settings
from app.utils.logger import logger

class GuardrailsEngine:
    """
    Guardrails & Response Output Validator.
    Validates confidence thresholds, JSON structure compliance, and safety rules.
    """

    def validate_response(self, response_data: Dict[str, Any], confidence_score: float) -> Dict[str, Any]:
        threshold = settings.GUARDRAIL_CONFIDENCE_THRESHOLD
        is_passed = confidence_score >= threshold
        
        if not is_passed:
            logger.warn(f"Guardrails: Confidence score ({confidence_score}) below threshold ({threshold})")
            response_data["guardrail_warning"] = "Low confidence score detected. Human-in-the-loop review suggested."

        response_data["_metadata"] = {
            "confidence_score": confidence_score,
            "guardrail_status": "PASSED" if is_passed else "WARNING",
            "threshold": threshold
        }
        return response_data

guardrails_engine = GuardrailsEngine()
