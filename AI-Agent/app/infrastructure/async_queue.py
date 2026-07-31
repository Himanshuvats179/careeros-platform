import asyncio
import uuid
from typing import Dict, Any, Callable
from app.utils.logger import logger

class AsyncTaskQueue:
    """
    In-Memory Asynchronous Background Task Queue & Job Tracker.
    Offloads heavy document chunking, embedding generation, and model evaluations from the HTTP thread.
    Can be seamlessly backed by Celery or Redis Queue (RQ) in multi-node clusters.
    """

    def __init__(self):
        self._jobs: Dict[str, Dict[str, Any]] = {}

    def submit_job(self, task_func: Callable, *args, **kwargs) -> str:
        job_id = f"job_{uuid.uuid4().hex[:10]}"
        self._jobs[job_id] = {
            "job_id": job_id,
            "status": "PENDING",
            "result": None,
            "error": None
        }

        asyncio.create_task(self._run_task(job_id, task_func, *args, **kwargs))
        logger.info(f"AsyncTaskQueue: Submitted background job ID '{job_id}'")
        return job_id

    async def _run_task(self, job_id: str, task_func: Callable, *args, **kwargs):
        self._jobs[job_id]["status"] = "RUNNING"
        try:
            if asyncio.iscoroutinefunction(task_func):
                result = await task_func(*args, **kwargs)
            else:
                result = task_func(*args, **kwargs)

            self._jobs[job_id]["status"] = "COMPLETED"
            self._jobs[job_id]["result"] = result
            logger.info(f"AsyncTaskQueue: Job '{job_id}' completed successfully.")
        except Exception as e:
            self._jobs[job_id]["status"] = "FAILED"
            self._jobs[job_id]["error"] = str(e)
            logger.error(f"AsyncTaskQueue: Job '{job_id}' failed: {e}")

    def get_job_status(self, job_id: str) -> Dict[str, Any]:
        return self._jobs.get(job_id, {"job_id": job_id, "status": "NOT_FOUND"})

async_task_queue = AsyncTaskQueue()
