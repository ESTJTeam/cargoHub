package cargohub.orchestratorservice.infrastructure.client.ai;

import cargohub.orchestratorservice.infrastructure.client.ai.dto.request.CalculateAiDeadlineRequestV1;
import cargohub.orchestratorservice.infrastructure.client.ai.dto.response.AiDeadlineResponseV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", path = "/v1/ai")
public interface AiClient {

    @PostMapping("/deadline/generate/{orderId}")
    AiDeadlineResponseV1 calculateDeadlineWithPromptData(
        @RequestBody CalculateAiDeadlineRequestV1 request);
}
