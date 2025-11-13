package cargohub.orchestratorservice.infrastructure.client.order;

import cargohub.orchestratorservice.infrastructure.client.order.dto.CreateOrderClientRequestV1;
import cargohub.orchestratorservice.infrastructure.client.order.dto.CreateOrderResponseV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "order-service", path = "/v1/orders")
public interface OrderClient {

    @PostMapping
    CreateOrderResponseV1 createOrder(CreateOrderClientRequestV1 request);

}
