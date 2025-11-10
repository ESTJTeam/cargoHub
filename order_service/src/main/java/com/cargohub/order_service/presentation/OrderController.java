package com.cargohub.order_service.presentation;

import com.cargohub.order_service.application.OrderService;
import com.cargohub.order_service.application.command.CreateOrderCommandV1;
import com.cargohub.order_service.application.command.OrderProductCommandV1;
import com.cargohub.order_service.application.command.UpdateOrderStatusCommandV1;
import com.cargohub.order_service.application.dto.CreateOrderResultV1;
import com.cargohub.order_service.common.success.BaseResponse;
import com.cargohub.order_service.common.success.BaseStatus;
import com.cargohub.order_service.domain.vo.OrderStatus;
import com.cargohub.order_service.presentation.dto.request.CreateOrderRequestV1;
import com.cargohub.order_service.presentation.dto.request.FirmInfoResponseV1;
import com.cargohub.order_service.presentation.dto.request.UpdateOrderStatusRequestV1;
import com.cargohub.order_service.presentation.dto.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CreateOrderResponseV1> createOrder(@RequestBody @Valid CreateOrderRequestV1 request) {

        CreateOrderCommandV1 commandV1 = new CreateOrderCommandV1(
                request.receiverId(),
                request.products().stream()
                        .map(p -> new OrderProductCommandV1(p.id(), p.quantity()))
                        .toList(),
                request.requestNote(),
                UUID.randomUUID() // todo: userId로 수정
        );

        CreateOrderResultV1 result = orderService.createOrder(commandV1);

        return BaseResponse.ok(CreateOrderResponseV1.from(result), BaseStatus.CREATED);
    }

    @GetMapping
    public BaseResponse<Page<ReadOrderSummaryResponseV1>>  readOrderPage(@PageableDefault(size = 10)Pageable pageable) {

        // todo: 애플리케이션 서비스 호출


        OrderStatusResponseV1 status = OrderStatusResponseV1.of(OrderStatus.PREPARING);

        ReadOrderSummaryResponseV1 responseV1 = new ReadOrderSummaryResponseV1(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                status,
                LocalDateTime.now()
        );

        return BaseResponse.ok(new PageImpl<>(List.of(responseV1)), BaseStatus.OK);
    }

    @GetMapping("/{id}")
    public BaseResponse<ReadOrderDetailResponseV1> readOrder(@PathVariable("id") UUID id) {

        OrderProductResponseV1 product = new OrderProductResponseV1(
                UUID.randomUUID(),
                "상품 명",
                1000000,
                BigDecimal.valueOf(10000)
        );

        FirmInfoResponseV1 supplier = new FirmInfoResponseV1(
                UUID.randomUUID(),
                "공급 업체 명",
                "인천광역시 연수구 테크노파크로 110 우지타워 2층"
        );

        FirmInfoResponseV1 receiver = new FirmInfoResponseV1(
                UUID.randomUUID(),
                "수령 업체 명",
                "인천광역시 연수구 테크노파크로 110 우지타워 2층"
        );

        ReadOrderDetailResponseV1 responseV1 = new ReadOrderDetailResponseV1(
                id,
                supplier,
                receiver,
                List.of(product),
                OrderStatusResponseV1.of(OrderStatus.PREPARING),
                "11월 5일 오전 납품 부탁드립니다.",
                LocalDateTime.now(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                null,
                null

        );

        return BaseResponse.ok(responseV1, BaseStatus.OK);
    }

    @DeleteMapping("/{id}/cancel")
    public BaseResponse<Void> cancelOrder(@PathVariable("id") UUID id) {


        return BaseResponse.ok(BaseStatus.OK);
    }

}
