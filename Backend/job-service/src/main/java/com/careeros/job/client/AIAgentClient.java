package com.careeros.job.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ai-agent-service", url = "${app.ai-agent.url:http://localhost:8000}")
public interface AIAgentClient {

    @PostMapping("/api/v1/ai/ats-score")
    Map<String, Object> calculateAtsScore(@RequestBody Map<String, Object> request);

    @PostMapping("/api/v1/ai/resume/improve")
    Map<String, Object> improveResume(@RequestBody Map<String, Object> request);

    @PostMapping("/api/v1/rag/search")
    Map<String, Object> searchRAG(@RequestBody Map<String, Object> request);

    @PostMapping("/api/v1/rag/ingest")
    Map<String, Object> ingestDocument(@RequestBody Map<String, Object> request);
}
