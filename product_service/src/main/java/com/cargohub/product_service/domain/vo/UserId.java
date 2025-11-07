package com.cargohub.product_service.domain.vo;

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
public class UserId {

    private UUID id;

    private UserId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("유효하지 않은 고객 ID 입니다");
        }
        this.id = id;
    }

    public static UserId of(UUID id) {
        return new UserId(id);
    }
}
