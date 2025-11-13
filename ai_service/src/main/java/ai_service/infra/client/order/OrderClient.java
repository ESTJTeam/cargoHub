package ai_service.infra.client.order;

import ai_service.infra.client.order.response.OrderForAiResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "order-service",
    url = "${clients.order.url}",
    configuration = ai_service.infra.config.FeignConfig.class
)
public interface OrderClient {

    @GetMapping("/v1/orders/{orderNum}")
    OrderForAiResponseV1 getOrderForAi(@PathVariable("orderNum") UUID orderNum);
}
