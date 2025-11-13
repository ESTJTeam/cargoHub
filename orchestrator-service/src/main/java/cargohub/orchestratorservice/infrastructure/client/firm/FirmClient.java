package cargohub.orchestratorservice.infrastructure.client.firm;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "firm-service")
public interface FirmClient {

}
