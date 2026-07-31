from typing import List, Dict, Any, Optional

class ConversationMemoryManager:
    """
    In-Memory & Session State Conversation Memory Manager.
    Tracks turn-by-turn conversation history and user working memory.
    """

    def __init__(self):
        self._sessions: Dict[str, List[Dict[str, str]]] = {}
        self._user_context: Dict[str, Dict[str, Any]] = {}

    def add_message(self, session_id: str, role: str, content: str):
        if session_id not in self._sessions:
            self._sessions[session_id] = []
        self._sessions[session_id].append({"role": role, "content": content})

    def get_history(self, session_id: str, limit: int = 10) -> List[Dict[str, str]]:
        messages = self._sessions.get(session_id, [])
        return messages[-limit:]

    def set_user_context(self, user_id: str, key: str, value: Any):
        if user_id not in self._user_context:
            self._user_context[user_id] = {}
        self._user_context[user_id][key] = value

    def get_user_context(self, user_id: str) -> Dict[str, Any]:
        return self._user_context.get(user_id, {})

    def clear_session(self, session_id: str):
        if session_id in self._sessions:
            del self._sessions[session_id]

memory_manager = ConversationMemoryManager()
