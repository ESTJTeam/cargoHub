package com.cargohub.order_service.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDeliveryId {

    private UUID id;

    private HubDeliveryId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("유효하지 않는 배송 ID 입니다.");
        }
        this.id = id;
    }

    public static HubDeliveryId of(UUID id) {
        return new HubDeliveryId(id);
    }
}
