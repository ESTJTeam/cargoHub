package user_server.user_server.application.dto.query;

import java.util.UUID;
import user_server.user_server.domain.vo.UserRole;

public record CreateDeliveryAdminQueryV1(
    UUID userId,
    String slackId,
    UUID hubId,
    UserRole userRole,
    int deliverySequenceNum

){}