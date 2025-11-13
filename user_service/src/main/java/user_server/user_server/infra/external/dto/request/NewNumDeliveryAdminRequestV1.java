package user_server.user_server.infra.external.dto.request;

import java.util.UUID;
import user_server.user_server.domain.vo.UserRole;

public record NewNumDeliveryAdminRequestV1(

    UUID hubId,
    UserRole userRole

){}
