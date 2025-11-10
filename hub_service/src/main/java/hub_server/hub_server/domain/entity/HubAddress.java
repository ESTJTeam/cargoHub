package hub_server.hub_server.domain.entity;

import hub_server.hub_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubAddress extends BaseEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "postal_code", length = 20)
    private String postalCode;        // 우편번호

    @Column(name = "country", length = 50)
    private String country;           // 나라

    @Column(name = "region", length = 50)
    private String region;            // 지역

    @Column(name = "city", length = 50)
    private String city;              // 시/군/구

    @Column(name = "district", length = 50)
    private String district;          // 읍/면/동

    @Column(name = "road_name", length = 100)
    private String roadName;          // 도로명

    @Column(name = "building_name", length = 100)
    private String buildingName;      // 건물명

    @Column(name = "detail_address", length = 100)
    private String detailAddress;     // 상세주소

    @Lob
    @Column(name = "full_address")
    private String fullAddress;       // 전체주소

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;      // 위도

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;     // 경도

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private Hub hub;
}
