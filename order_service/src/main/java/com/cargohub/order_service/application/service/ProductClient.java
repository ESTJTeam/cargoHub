package com.cargohub.order_service.application.service;

import com.cargohub.order_service.application.command.OrderProductCommandV1;
import com.cargohub.order_service.application.service.product.BilkProductQueryRequestV1;
import com.cargohub.order_service.application.service.product.BulkProductQueryResponseV1;
import com.cargohub.order_service.application.service.product.UpdateProductStockRequestV1;
import com.cargohub.order_service.domain.vo.ProductId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "product-service", url="http://localhost:19100", path="/v1/products")
public interface ProductClient {

    @PostMapping("/bulk-query")
    BulkProductQueryResponseV1 getProducts(@RequestBody BilkProductQueryRequestV1 request);

    @PostMapping("/decrease")
    void decreaseStock(@RequestBody UpdateProductStockRequestV1 request);

    @PostMapping("/increase")
    void increaseStock(@RequestBody List<OrderProductCommandV1> requestV1s);



}
