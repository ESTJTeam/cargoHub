package com.cargohub.firm_service.domain.vo;

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
public class HubId {

    private UUID hubId;

    private HubId(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("유효하지 않는 허브 ID입니다.");
        }

        this.hubId = id;
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}
