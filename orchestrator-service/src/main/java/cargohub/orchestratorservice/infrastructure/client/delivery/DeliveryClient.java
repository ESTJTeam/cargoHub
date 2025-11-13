package cargohub.orchestratorservice.infrastructure.client.delivery;

import cargohub.orchestratorservice.infrastructure.client.delivery.dto.DeliveryRequestV1;
import cargohub.orchestratorservice.infrastructure.client.delivery.dto.DeliveryResponseV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "delivery-service", path = "/v1")
public interface DeliveryClient {

    @PostMapping("/internal/deliveries/from-order")
    DeliveryResponseV1 createDelivery(DeliveryRequestV1 deliveryRequestV1);

}
