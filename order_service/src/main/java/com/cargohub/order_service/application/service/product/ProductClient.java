package com.cargohub.order_service.application.service.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url="http://localhost:19100", path="/v1/products")
public interface ProductClient {

    @PostMapping("/bulk-query")
    BulkProductQueryResponseV1 getProducts(@RequestBody BilkProductQueryRequestV1 request);

    @PostMapping("/decrease")
    void decreaseStock(@RequestBody UpdateProductStockRequestV1 request);

    @PostMapping("/increase")
    void increaseStock(@RequestBody UpdateProductStockRequestV1 request);



}
