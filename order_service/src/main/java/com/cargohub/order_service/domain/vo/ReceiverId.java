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
public class ReceiverId {

    private UUID id;

    private ReceiverId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("유효하지 않은 업체 ID 입니다.");
        }
        this.id = id;
    }

    public static ReceiverId of(UUID id) {
        return new ReceiverId(id);
    }
}
