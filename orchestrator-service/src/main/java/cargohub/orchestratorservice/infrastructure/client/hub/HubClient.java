package cargohub.orchestratorservice.infrastructure.client.hub;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-service")
public interface HubClient {

}
