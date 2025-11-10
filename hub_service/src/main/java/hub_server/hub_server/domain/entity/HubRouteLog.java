package hub_server.hub_server.domain.entity;

import hub_server.hub_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_route_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRouteLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_hub_id")
    private Hub startHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_hub_id")
    private Hub endHub;

    @Column(nullable = false)
    private Integer totalDuration;

    @Column(nullable = false)
    private Integer totalDistance;

    @OneToMany(mappedBy = "hubRouteLog",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("sequenceOrder ASC")
    private List<HubRouteLogStop> stops = new ArrayList<>();
}
