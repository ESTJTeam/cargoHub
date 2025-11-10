package com.cargohub.firm_service.domain.entity;

import com.cargohub.firm_service.common.BaseEntity;
import com.cargohub.firm_service.domain.vo.HubId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_firm")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Firm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "firm_address_id")
    private FirmAddress address;            // 업체 주소 아이디(FK) (PRODUCER,RECEIVER)

    @Column(name = "name", length = 20, nullable = false)
    private String name;                   // 업체명

    @Column(name = "hub_id")
    private HubId hubId;                    // 허브 id

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 15)
    private FirmType type;                 // 업체 타입

    public Firm(String name, String type, HubId hubId, FirmAddress address) {
        this.name = name;
        this.address = address;
        this.hubId = hubId;
        this.type = FirmType.valueOf(type);
    }

    public static Firm ofNewFirm(String name, String type, HubId hubId, FirmAddress address) {
        return new Firm(name, type, hubId, address);
    }

    public void update(String name, String type, HubId hubId, FirmAddress address) {
        this.name = name;
        this.address = address;
        this.hubId = hubId;
        this.type = FirmType.valueOf(type);
    }
}
