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
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_hub_id", nullable = false)
    private Hub endHub;

    // 총 소요 시간 (분)
    @Column(nullable = false)
    private Integer totalDuration;

    // 총 거리 (km)
    @Column(nullable = false)
    private Double totalDistance;

    @OneToMany(mappedBy = "hubRouteLog",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("sequenceOrder ASC")
    private List<HubRouteLogStop> stops = new ArrayList<>();

    public static HubRouteLog create(Hub startHub, Hub endHub, Integer totalDuration, Double totalDistance) {
        HubRouteLog routeLog = new HubRouteLog();
        routeLog.startHub = startHub;
        routeLog.endHub = endHub;
        routeLog.totalDuration = totalDuration;
        routeLog.totalDistance = totalDistance;
        return routeLog;
    }

    public void addStop(Hub hub, Integer sequenceOrder) {
        HubRouteLogStop stop = HubRouteLogStop.create(this, hub, sequenceOrder);
        this.stops.add(stop);
    }

    public void setStops(List<Hub> hubs) {
        this.stops.clear();
        for (int i = 0; i < hubs.size(); i++) {
            addStop(hubs.get(i), i);
        }
    }

    public void delete(UUID deletedBy) {
        super.delete(deletedBy);
        // 연관된 stops도 함께 논리 삭제
        this.stops.forEach(stop -> stop.delete(deletedBy));
    }
}
