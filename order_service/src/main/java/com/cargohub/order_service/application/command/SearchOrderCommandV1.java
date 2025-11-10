package com.cargohub.order_service.application.command;

import com.cargohub.order_service.domain.vo.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record SearchOrderCommandV1(

        // 허브 관리자 or 마스터가 조회 시
        UUID supplierId,

        // 업체 담당자나 배송 담당자 기준
        UUID receiverId,

        OrderStatus status,

        UUID createdBy,

        String requestNote,

        LocalDateTime startDate,

        LocalDateTime endDate
) {

}