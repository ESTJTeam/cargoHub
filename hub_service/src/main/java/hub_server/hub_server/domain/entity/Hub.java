package hub_server.hub_server.domain.entity;

import hub_server.hub_server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    @OneToOne(mappedBy = "hub", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HubAddress hubAddress;
}
