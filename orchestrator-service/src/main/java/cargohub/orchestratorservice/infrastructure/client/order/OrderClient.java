package cargohub.orchestratorservice.infrastructure.client.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service")
public interface OrderClient {

}
