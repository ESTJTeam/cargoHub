package com.cargohub.order_service.presentation;

import com.cargohub.order_service.application.OrderService;
import com.cargohub.order_service.application.command.CreateOrderCommandV1;
import com.cargohub.order_service.application.command.DeleteOrderCommandV1;
import com.cargohub.order_service.application.command.OrderProductCommandV1;
import com.cargohub.order_service.application.command.UpdateOrderStatusCommandV1;
import com.cargohub.order_service.application.dto.CreateOrderResultV1;
import com.cargohub.order_service.application.dto.ReadOrderDetailResultV1;
import com.cargohub.order_service.common.success.BaseResponse;
import com.cargohub.order_service.common.success.BaseStatus;
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

        CreateOrderResponseV1 responseV1 = new CreateOrderResponseV1(
                result.id(),
                result.supplierId(),
                result.receiverId(),
                result.products().stream()
                        .map(OrderProductResponseV1::from)
                        .toList(),
                result.requestNote(),
                result.createdAt()
        );

        return BaseResponse.ok(responseV1, BaseStatus.CREATED);
    }

    @GetMapping
    public BaseResponse<Page<ReadOrderSummaryResponseV1>>  readOrderPage(@PageableDefault(size = 10)Pageable pageable) {

        // todo: 애플리케이션 서비스 호출


        ReadOrderSummaryResponseV1 responseV1 = new ReadOrderSummaryResponseV1(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                LocalDateTime.now()
        );

        return BaseResponse.ok(new PageImpl<>(List.of(responseV1)), BaseStatus.OK);
    }

    @GetMapping("/{id}")
    public BaseResponse<ReadOrderDetailResponseV1> readOrder(@PathVariable("id") UUID id) {

        ReadOrderDetailResultV1 result = orderService.readOrder(id);

        ReadOrderDetailResponseV1 responseV1 = new ReadOrderDetailResponseV1(
                result.id(),
                FirmInfoResponseV1.from(result.supplier()),
                FirmInfoResponseV1.from(result.receiver()),
                result.products().stream()
                        .map(OrderProductResponseV1::from)
                        .toList(),
                OrderStatusResponseV1.from(result.status()),
                result.requestNote(),
                result.createdAt(),
                result.createdBy(),
                result.updatedAt(),
                result.updatedBy(),
                result.deletedAt(),
                result.deletedBy()
        );

        return BaseResponse.ok(responseV1, BaseStatus.OK);
    }


    @PatchMapping("/{id}/status")
    public BaseResponse<Void> updateOrderStatus(@PathVariable("id") UUID id, @RequestBody @Valid UpdateOrderStatusRequestV1 request) {

        UpdateOrderStatusCommandV1 commandV1 = new UpdateOrderStatusCommandV1(
                id,
                request.status(),
                UUID.randomUUID() // todo: 수정자 ID
        );

        orderService.updateOrderStatus(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/{id}/cancel")
    public BaseResponse<Void> cancelOrder(@PathVariable("id") UUID id) {

        DeleteOrderCommandV1 commandV1 = new DeleteOrderCommandV1(
                id,
                UUID.randomUUID() // todo: 삭제자 ID
        );

        orderService.cancelOrder(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

}
