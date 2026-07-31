import time
from typing import Dict, Any, Optional
from app.cache.semantic_cache import semantic_cache
from app.planner.task_planner import task_planner
from app.tools.registry import tool_registry
from app.rag.pipeline import rag_pipeline
from app.agents.reflection_agent import reflection_agent
from app.evaluation.guardrails import guardrails_engine
from app.evaluation.feedback import rag_evaluator
from app.infrastructure.telemetry import telemetry_tracker
from app.memory.conversation_memory import memory_manager
from app.utils.logger import logger

class AgentOrchestrator:
    """
    Multi-Agent Workflow State Orchestrator.
    Lifecycle:
    Semantic Cache Check -> Task Planning -> Tool Execution -> 2-Stage RAG -> Agent Gen -> Reflection -> Guardrails -> Telemetry.
    """

    async def execute_workflow(
        self,
        user_id: str,
        session_id: str,
        goal: str,
        input_data: Dict[str, Any],
        generator_func: Any
    ) -> Dict[str, Any]:
        start_time = time.time()
        logger.info(f"AgentOrchestrator: Initiating workflow for user '{user_id}' session '{session_id}' goal '{goal}'")

        # 0. Check Semantic Vector Cache
        cached = semantic_cache.get(goal)
        if cached:
            telemetry_tracker.record_request(
                latency=time.time() - start_time,
                is_cache_hit=True,
                prompt_text=goal,
                response_text=str(cached),
                is_guardrail_pass=True
            )
            return cached

        # 1. Step 1: Task Planning
        plan = task_planner.plan_task(goal=goal, context=input_data)
        logger.info(f"AgentOrchestrator: Planned {len(plan)} execution steps")

        # 2. Step 2: Tool Execution
        tool_results = {}
        for step in plan:
            if "tool" in step:
                tool_name = step["tool"]
                sample_input = input_data.get("resume_text", input_data.get("goal", ""))
                res = tool_registry.execute_tool(tool_name, text=sample_input)
                tool_results[tool_name] = res

        # 3. Step 3: 2-Stage RAG Retrieval
        top_chunks = rag_pipeline.retrieve_and_rerank(query=goal, top_k_retrieval=5, top_n_rerank=2)
        context_prompt = rag_pipeline.assemble_context_prompt(query=goal, top_chunks=top_chunks)

        # 4. Step 4: Agent Generation
        generated_result = await generator_func()

        # 5. Step 5: Reflection & Self-Evaluation
        str_res = str(generated_result)
        reflection = await reflection_agent.reflect(query=goal, context_chunks=top_chunks, generated_response=str_res)

        # 6. Step 6: Guardrails & Response Validation
        if isinstance(generated_result, dict):
            final_output = guardrails_engine.validate_response(generated_result, reflection["overall_confidence"])
        else:
            final_output = {"result": generated_result}
            final_output = guardrails_engine.validate_response(final_output, reflection["overall_confidence"])

        # 7. Step 7: Evaluate RAG Triad Metrics
        rag_metrics = rag_evaluator.evaluate_rag_triad(query=goal, context_chunks=top_chunks, answer=str_res)

        # 8. Step 8: Update Conversation Memory
        memory_manager.add_message(session_id, "user", goal)
        memory_manager.add_message(session_id, "assistant", str_res[:200])

        final_output["_reflection"] = reflection
        final_output["_rag_eval"] = rag_metrics
        final_output["_rag_retrieved_count"] = len(top_chunks)

        # 9. Step 9: Cache response & Record Telemetry
        semantic_cache.set(goal, final_output)
        telemetry_tracker.record_request(
            latency=time.time() - start_time,
            is_cache_hit=False,
            prompt_text=goal,
            response_text=str_res,
            is_guardrail_pass=reflection["overall_confidence"] >= 0.70
        )
        return final_output

agent_orchestrator = AgentOrchestrator()
