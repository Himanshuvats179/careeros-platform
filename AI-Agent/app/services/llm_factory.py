import os
import json
from typing import Any, Dict, Optional
from app.config.settings import settings
from app.utils.logger import logger

class LLMFactory:
    """
    Unified Factory for AI Model Providers:
    1. OpenAI (gpt-4o / gpt-4-turbo)
    2. AWS Bedrock (Claude 3.5 Sonnet / Llama 3)
    3. Fallback Mock Provider (deterministic offline JSON generator)
    """

    @staticmethod
    def get_llm():
        provider = os.getenv("LLM_PROVIDER", "openai").lower()

        if provider == "bedrock" or settings.USE_AWS_BEDROCK:
            try:
                import boto3
                from langchain_community.chat_models import BedrockChat
                boto_session = boto3.Session(
                    aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
                    aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
                    region_name=settings.AWS_REGION
                )
                bedrock_client = boto_session.client("bedrock-runtime")
                model_id = os.getenv("BEDROCK_MODEL_ID", "anthropic.claude-3-5-sonnet-20240620-v1:0")
                logger.info(f"Initializing AWS Bedrock LLM with model: {model_id}")
                return BedrockChat(
                    client=bedrock_client,
                    model_id=model_id,
                    model_kwargs={"temperature": settings.TEMPERATURE, "max_tokens": 2048}
                )
            except Exception as e:
                logger.warn(f"Failed to initialize AWS Bedrock ({e}). Falling back to OpenAI/Mock.")

        if settings.OPENAI_API_KEY and settings.OPENAI_API_KEY != "mock-openai-key":
            try:
                from langchain_openai import ChatOpenAI
                logger.info(f"Initializing OpenAI LLM with model: {settings.OPENAI_MODEL}")
                return ChatOpenAI(
                    api_key=settings.OPENAI_API_KEY,
                    model=settings.OPENAI_MODEL,
                    temperature=settings.TEMPERATURE
                )
            except Exception as e:
                logger.warn(f"Failed to initialize OpenAI client ({e}). Falling back to Mock.")

        logger.info("Using Fallback Mock LLM Provider.")
        return MockLLM()

class MockLLM:
    """Fallback LLM invocation wrapper when cloud API keys are absent."""
    def invoke(self, prompt: str) -> "MockResponse":
        logger.info("MockLLM: Simulating LLM response.")
        return MockResponse(
            json.dumps({
                "status": "success",
                "message": "AI generation executed successfully via CareerOS fallback engine.",
                "analysis": "High candidate skill alignment detected across backend services and AI platform requirements."
            })
        )

class MockResponse:
    def __init__(self, content: str):
        self.content = content

llm_factory = LLMFactory()
