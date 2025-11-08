package com.cargohub.delivery_service.domain.entity;

import com.cargohub.delivery_service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_hub_delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDelivery extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;                         // 아이디

    @Column(name = "order_id", nullable = false)
    private UUID orderId;                    // 주문 id

    @Column(name = "start_hub_id", nullable = false)
    private UUID startHubId;                 // 출발 허브 id

    @Column(name = "end_hub_id", nullable = false)
    private UUID endHubId;                   // 목적지 허브 id

    @Column(name = "delivery_person_id")
    private UUID deliveryPersonId;           // 허브 배송담당자 id

    @Column(name = "delivery_sequence_num")
    private Integer deliverySequenceNum;     // 배송 순서

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 15)
    private DeliveryStatus deliveryStatus;   // 배송 현황

    @Column(name = "expected_distance")
    private Double expectedDistance;         // 예상 거리

    @Column(name = "expected_delivery_duration")
    private LocalDateTime expectedDeliveryDuration; // 예상 소요 시간 (날짜/시간 기반)

    @Column(name = "distance")
    private Double distance;                 // 실제 거리

    @Column(name = "delivery_duration")
    private LocalDateTime deliveryDuration;  // 실제 소요 시간
}
