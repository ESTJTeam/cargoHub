package com.cargohub.order_service.application.command;

import com.cargohub.order_service.domain.vo.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record SearchOrderCommandV1(

        UUID supplierId,

        UUID receiverId,

        OrderStatus status,

        UUID createdBy,

        String requestNote,

        LocalDateTime startDate,

        LocalDateTime endDate,

        UserInfo user
) {

}