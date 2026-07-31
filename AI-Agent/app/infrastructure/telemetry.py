import time
from typing import Dict, Any
from app.utils.logger import logger

class AITelemetryTracker:
    """
    AI Observability & Telemetry Metrics Engine.
    Tracks LLM request latency, token consumption estimates, cache hit ratios, and guardrail pass rates.
    Ready for Prometheus metric collection or OpenTelemetry tracing exporters.
    """

    def __init__(self):
        self.metrics = {
            "total_requests": 0,
            "cache_hits": 0,
            "cache_misses": 0,
            "total_latency_seconds": 0.0,
            "estimated_tokens_consumed": 0,
            "guardrail_passes": 0,
            "guardrail_warnings": 0
        }

    def record_request(self, latency: float, is_cache_hit: bool, prompt_text: str, response_text: str, is_guardrail_pass: bool):
        self.metrics["total_requests"] += 1
        self.metrics["total_latency_seconds"] += latency

        if is_cache_hit:
            self.metrics["cache_hits"] += 1
        else:
            self.metrics["cache_misses"] += 1

        # Token estimation heuristic (approx 4 chars per token)
        estimated_tokens = (len(prompt_text) + len(response_text)) // 4
        self.metrics["estimated_tokens_consumed"] += estimated_tokens

        if is_guardrail_pass:
            self.metrics["guardrail_passes"] += 1
        else:
            self.metrics["guardrail_warnings"] += 1

        logger.info(f"Telemetry: Latency={latency*1000:.2f}ms, CacheHit={is_cache_hit}, EstimatedTokens={estimated_tokens}")

    def get_summary(self) -> Dict[str, Any]:
        total = self.metrics["total_requests"] or 1
        avg_latency = self.metrics["total_latency_seconds"] / total
        cache_hit_ratio = self.metrics["cache_hits"] / total

        return {
            "total_requests": self.metrics["total_requests"],
            "avg_latency_ms": round(avg_latency * 1000, 2),
            "cache_hit_ratio": round(cache_hit_ratio, 4),
            "total_tokens_consumed": self.metrics["estimated_tokens_consumed"],
            "guardrail_pass_rate": round(self.metrics["guardrail_passes"] / total, 4)
        }

telemetry_tracker = AITelemetryTracker()
