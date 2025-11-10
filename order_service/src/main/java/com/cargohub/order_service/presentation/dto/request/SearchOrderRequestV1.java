package com.cargohub.order_service.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record SearchOrderRequestV1(

        // 허브 관리자 or 마스터가 조회 시
        UUID supplierId,

        UUID receiverId,

        String status,

        UUID createdBy,

        String requestNote,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @PastOrPresent(message = "조회 시작일은 미래일 수 없습니다.")
        LocalDateTime startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate
) {

}