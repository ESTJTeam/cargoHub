package hub_server.hub_server.domain.entity;

import hub_server.hub_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubInfo extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_hub_id")
    private Hub startHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_hub_id")
    private Hub endHub;

    // 분단위 계산
    @Column(nullable = false)
    private Integer deliveryDuration;

    @Column(nullable = false)
    private Double distance;
}
