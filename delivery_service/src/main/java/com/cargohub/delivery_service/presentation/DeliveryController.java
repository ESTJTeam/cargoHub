package com.cargohub.delivery_service.presentation;

import com.cargohub.delivery_service.application.DeliveryService;
import com.cargohub.delivery_service.application.dto.request.DeliveryRequestV1;
import com.cargohub.delivery_service.application.dto.response.DeliveryResponseV1;
import com.cargohub.delivery_service.common.success.BaseResponse;
import com.cargohub.delivery_service.common.success.BaseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class DeliveryController {

    private DeliveryService deliveryService;

    @PostMapping("/internal/deliveries/from-order")
    public BaseResponse<DeliveryResponseV1> createDelivery(DeliveryRequestV1 deliveryRequestV1) {

        DeliveryResponseV1 response = deliveryService.createDelivery(deliveryRequestV1);

        return BaseResponse.ok(response, BaseStatus.CREATED);
    }
}
