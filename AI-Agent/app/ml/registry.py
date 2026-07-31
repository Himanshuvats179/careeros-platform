from abc import ABC, abstractmethod
from typing import Dict, Any, Optional
from app.utils.logger import logger

class BaseModelProvider(ABC):
    """Abstract Interface for Custom ML & Deep Learning Models."""

    @abstractmethod
    def predict(self, input_data: Any) -> Dict[str, Any]:
        pass

class ModelRegistry:
    """
    Enterprise ML Model Registry.
    Decouples custom PyTorch, HuggingFace, ONNX, or scikit-learn models from business logic.
    Enables zero-downtime model switching via Dependency Inversion.
    """

    def __init__(self):
        self._registry: Dict[str, BaseModelProvider] = {}

    def register(self, model_name: str, provider: BaseModelProvider):
        self._registry[model_name] = provider
        logger.info(f"ModelRegistry: Registered custom ML model '{model_name}'")

    def get_model(self, model_name: str) -> Optional[BaseModelProvider]:
        model = self._registry.get(model_name)
        if not model:
            logger.warn(f"ModelRegistry: Model '{model_name}' not found.")
        return model

model_registry = ModelRegistry()
