package cargohub.orchestratorservice.infrastructure.client.slack;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "slack-service")
public interface SlackClient {

}
