package user_server.user_server.presentation.success.dto.response;

import java.util.UUID;
import user_server.user_server.application.dto.query.CreateDeliveryAdminQueryV1;
import user_server.user_server.domain.vo.UserRole;

public record DeliveryAdminResponseV1(
    UUID userId,
    UUID hubId,
    String slackId,
    UserRole userRole,
    int deliverySequenceNum
    )
{
    public static DeliveryAdminResponseV1 from(CreateDeliveryAdminQueryV1 query) {
        return new DeliveryAdminResponseV1(query.userId(), query.hubId(), query.slackId(), query.userRole(), query.deliverySequenceNum());
    }
}

