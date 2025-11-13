package slack_service.infrastructure.client.ai;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import slack_service.infrastructure.client.ai.response.AiDeadlineResponseV1;

@FeignClient(
    name = "ai-service",
    url = "${clients.ai.url}",
    configuration = slack_service.infrastructure.config.FeignConfig.class
)
public interface AiClient {

    @PostMapping("/v1/ai/deadline/generate/{orderId}")
    AiDeadlineResponseV1 generateDeadlineByOrderId(@PathVariable("orderId") UUID orderId);
}
