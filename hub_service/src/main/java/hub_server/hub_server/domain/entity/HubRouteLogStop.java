package hub_server.hub_server.domain.entity;

import hub_server.hub_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_route_log_stop")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRouteLogStop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_route_log_id", nullable = false)
    private HubRouteLog hubRouteLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @Column(nullable = false)
    private Integer sequenceOrder;

    public static HubRouteLogStop create(HubRouteLog hubRouteLog, Hub hub, Integer sequenceOrder) {
        HubRouteLogStop stop = new HubRouteLogStop();
        stop.hubRouteLog = hubRouteLog;
        stop.hub = hub;
        stop.sequenceOrder = sequenceOrder;
        return stop;
    }

    public void delete(UUID deletedBy) {
        super.delete(deletedBy);
    }
}
