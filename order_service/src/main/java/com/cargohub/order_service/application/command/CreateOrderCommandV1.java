package com.cargohub.order_service.application.command;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommandV1(
        UUID receiverId,
        List<OrderProductCommandV1> products,
        String requestNote,
        UserInfo user
) {

}
