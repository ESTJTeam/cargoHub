package user_server.user_server.application.dto.command;

import java.util.UUID;
import user_server.user_server.domain.vo.UserRole;
import user_server.user_server.infra.external.dto.request.NewNumDeliveryAdminRequestV1;

public record NewNumDeliveryAdminCommandV1(
    UUID hubId,
    UserRole userRole
){
    public static NewNumDeliveryAdminCommandV1 from(NewNumDeliveryAdminRequestV1 request) {
        return new NewNumDeliveryAdminCommandV1(request.hubId(), request.userRole());

    }

}

