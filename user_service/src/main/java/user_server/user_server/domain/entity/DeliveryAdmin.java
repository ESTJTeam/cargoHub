package user_server.user_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery_admin")
public class DeliveryAdmin extends BaseEntity {

    @Id
    @Column(name = "delivery_admin_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    int deliverySequenceNum;


    public DeliveryAdmin(String slackId, UUID hubId, Role role, int deliverySequenceNum) {
        this.slackId = slackId;
        this.hubId = hubId;
        this.role = role;
        this.deliverySequenceNum = deliverySequenceNum;
    }
}
