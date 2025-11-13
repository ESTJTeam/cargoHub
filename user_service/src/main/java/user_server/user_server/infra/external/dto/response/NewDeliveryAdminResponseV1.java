package user_server.user_server.infra.external.dto.response;

import java.util.UUID;
import user_server.user_server.domain.vo.UserRole;

public record NewDeliveryAdminResponseV1 (

    UUID hubId,
    String slackId,
    UserRole userRole,
    int sequenceNumber

){ }
