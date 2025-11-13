package cargohub.orchestratorservice.infrastructure.client.product;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductClient {

}
