package cargohub.orchestratorservice.infrastructure.client.ai;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ai-service")
public interface AiClient {
}
