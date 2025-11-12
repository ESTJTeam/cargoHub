package hub_server.hub_server.domain.entity;

import hub_server.hub_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID hubManagerId;

    @Setter
    @OneToOne(mappedBy = "hub", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HubAddress hubAddress;

    public static Hub create(String name, UUID hubManagerId) {

        Hub hub = new Hub();
        hub.name = name;
        hub.hubManagerId = hubManagerId;
        return hub;
    }

    public void update(String name, UUID hubManagerId) {

        this.name = name;
        this.hubManagerId = hubManagerId;
    }
}
