from typing import Dict, Optional
from app.utils.logger import logger

class PromptVersionManager:
    """
    Enterprise System Prompt Version Manager & Governance Engine.
    Manages semantic prompt versions (e.g. 'v1.0.0', 'v1.2.0') with fallbacks.
    """

    def __init__(self):
        self._prompts: Dict[str, Dict[str, str]] = {}

    def register_prompt(self, prompt_name: str, version: str, template: str):
        if prompt_name not in self._prompts:
            self._prompts[prompt_name] = {}
        self._prompts[prompt_name][version] = template
        logger.info(f"PromptVersionManager: Registered prompt '{prompt_name}' version '{version}'")

    def get_prompt(self, prompt_name: str, version: str = "latest") -> str:
        versions = self._prompts.get(prompt_name, {})
        if not versions:
            raise KeyError(f"Prompt '{prompt_name}' not registered.")

        if version == "latest":
            latest_ver = sorted(versions.keys())[-1]
            return versions[latest_ver]

        if version in versions:
            return versions[version]

        logger.warn(f"Prompt '{prompt_name}' version '{version}' not found. Falling back to latest version.")
        latest_ver = sorted(versions.keys())[-1]
        return versions[latest_ver]

prompt_version_manager = PromptVersionManager()
