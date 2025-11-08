package com.cargohub.order_service.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestV1(

        @NotNull(message = "수령 업체 ID는 필수 입니다.")
        UUID receiverId,

        String requestNote,

        @NotEmpty(message = "주문 상품 목록은 비어 있을 수 없습니다.")
        List<@Valid OrderProductRequestV1> products
) {

}
