package com.cargohub.order_service.presentation;

import com.cargohub.order_service.application.OrderService;
import com.cargohub.order_service.application.command.*;
import com.cargohub.order_service.application.dto.CreateOrderResultV1;
import com.cargohub.order_service.application.dto.ReadOrderDetailResultV1;
import com.cargohub.order_service.application.dto.ReadOrderSummaryResultV1;
import com.cargohub.order_service.common.JwtUtil;
import com.cargohub.order_service.common.success.BaseResponse;
import com.cargohub.order_service.common.success.BaseStatus;
import com.cargohub.order_service.domain.vo.OrderStatus;
import com.cargohub.order_service.application.service.user.UserInfoResponseV1;
import com.cargohub.order_service.presentation.dto.request.CreateOrderRequestV1;
import com.cargohub.order_service.presentation.dto.request.FirmInfoResponseV1;
import com.cargohub.order_service.presentation.dto.request.SearchOrderRequestV1;
import com.cargohub.order_service.presentation.dto.request.UpdateOrderStatusRequestV1;
import com.cargohub.order_service.presentation.dto.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final JwtUtil jwtUtil;
//    private final UserClient userClient;

    @ModelAttribute("userInfo")
    public UserInfoResponseV1 getUser(@RequestHeader(value = "Authorization", required = false) String accessToken) {
//        return userClient.getUser(accessToken);
        if (accessToken == null || accessToken.isBlank()) {
            return UserInfoResponseV1.anonymous();
        }
        return jwtUtil.parseJwt(accessToken);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CreateOrderResponseV1> createOrder(@RequestBody @Valid CreateOrderRequestV1 request) {

        CreateOrderCommandV1 commandV1 = new CreateOrderCommandV1(
            request.supplierId(),
            request.receiverId(),
            request.products().stream()
                .map(p -> new CreateOrderCommandV1.OrderProductInfo(p.id(), p.name(), p.quantity(), p.price()))
                .toList(),
            request.requestNote(),
            request.createdBy()
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
    public BaseResponse<Page<ReadOrderSummaryResponseV1>>  readOrderPage(@ModelAttribute SearchOrderRequestV1 search, @PageableDefault(size = 10)Pageable pageable, @ModelAttribute("userInfo") UserInfoResponseV1 userInfoResponse) {

        SearchOrderCommandV1 searchCommandV1 = new SearchOrderCommandV1(
                search.supplierId(),
                search.receiverId(),
                search.status() != null ? OrderStatus.valueOf(search.status()) : null,
                search.createdBy(),
                search.requestNote(),
                search.startDate(),
                search.endDate(),
                new UserInfo(userInfoResponse.userId(), userInfoResponse.role())
        );

        // todo: 사용자 정보 필요
        Page<ReadOrderSummaryResultV1> orderPage = orderService.readOrderPage(searchCommandV1, pageable);

        return BaseResponse.ok(orderPage.map(ReadOrderSummaryResponseV1::from), BaseStatus.OK);
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
    public BaseResponse<Void> updateOrderStatus(@PathVariable("id") UUID id, @RequestBody @Valid UpdateOrderStatusRequestV1 request, @RequestHeader(value = "Authorization") String accessToken) {

        UpdateOrderStatusCommandV1 commandV1 = new UpdateOrderStatusCommandV1(
                id,
                request.status()
        );

        orderService.updateOrderStatus(commandV1, accessToken);

        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/{id}/cancel")
    public BaseResponse<Void> cancelOrder(@PathVariable("id") UUID id, @ModelAttribute("userInfo") UserInfoResponseV1 userInfoResponse) {

        DeleteOrderCommandV1 commandV1 = new DeleteOrderCommandV1(
                id,
                new UserInfo(userInfoResponse.userId(), userInfoResponse.role())
        );

        orderService.cancelOrder(commandV1);

        return BaseResponse.ok(BaseStatus.OK);
    }

}
