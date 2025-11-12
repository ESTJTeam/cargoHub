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
import user_server.user_server.domain.vo.UserRole;

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
    private UserRole userRole;

    int deliverySequenceNum;


    public DeliveryAdmin(String slackId, UUID hubId, UserRole role, int deliverySequenceNum) {
        this.slackId = slackId;
        this.hubId = hubId;
        this.userRole = role;
        this.deliverySequenceNum = deliverySequenceNum;
    }

    public static DeliveryAdmin create(String slackId, UUID hubId, UserRole role, int deliverySequenceNum) {
        return new DeliveryAdmin(slackId, hubId, role, deliverySequenceNum);
    }
}
