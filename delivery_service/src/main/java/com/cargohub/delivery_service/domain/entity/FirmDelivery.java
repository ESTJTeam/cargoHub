package com.cargohub.delivery_service.domain.entity;

import com.cargohub.delivery_service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_firm_delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FirmDelivery extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;                         // 아이디

    @Column(name = "order_id", nullable = false)
    private UUID orderId;                    // 주문 id

    @Column(name = "start_hub_id", nullable = false)
    private UUID startHubId;                 // 출발 허브 id

    @Column(name = "delivery_address_id", columnDefinition = "TEXT")
    private String deliveryAddressId;        // 배송지 주소 id (문자/JSON 등)

    @Column(name = "delivery_person_id")
    private UUID deliveryPersonId;           // 업체 배송담당자 id

    @Column(name = "user_name", length = 15)
    private String userName;                 // 수령인 이름

    @Column(name = "user_slack_id", length = 20)
    private String userSlackId;              // 수령인 슬랙 아이디

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 15)
    private DeliveryStatus deliveryStatus;   // 배송 현황
}
