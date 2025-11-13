package cargohub.orchestratorservice.infrastructure.client.product;

import cargohub.orchestratorservice.infrastructure.client.product.dto.BulkProductQueryRequestV1;
import cargohub.orchestratorservice.infrastructure.client.product.dto.BulkProductQueryResponseV1;
import cargohub.orchestratorservice.infrastructure.client.product.dto.CheckProductStockRequestV1;
import cargohub.orchestratorservice.infrastructure.client.product.dto.UpdateProductStockRequestV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", path="/v1/products")
public interface ProductClient {

    @PostMapping("/bulk-query")
    BulkProductQueryResponseV1 getProducts(@RequestBody BulkProductQueryRequestV1 request);

    @PostMapping("/check-stock")
    void checkStock(@RequestBody CheckProductStockRequestV1 request);

    @PostMapping("/decrease")
    void decreaseStock(@RequestBody UpdateProductStockRequestV1 request);

    @PostMapping("/increase")
    void increaseStock(@RequestBody UpdateProductStockRequestV1 request);

}
