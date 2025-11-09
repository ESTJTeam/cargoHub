package com.cargohub.delivery_service.domain.entity;

public enum DeliveryStatus {

    READY,          // 준비
    PICKED_UP,      // 픽업 완료
    IN_TRANSIT,     // 이동 중
    ARRIVED,        // 도착
    COMPLETED,      // 인계 완료
    CANCELED;       // 취소
}
