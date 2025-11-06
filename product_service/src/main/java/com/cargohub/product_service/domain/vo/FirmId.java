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
public class FirmId {

    private UUID id;

    private FirmId(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("유효하지 않는 업체 ID입니다.");
        }

        this.id = id;
    }

    public static FirmId of(UUID id) {
        return new FirmId(id);
    }
}
