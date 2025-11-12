package user_server.user_server.presentation.success.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import user_server.user_server.application.dto.command.DeliveryAminCommandV1;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.vo.UserRole;

public record DeliverAdminRequestV1 (

    @NotBlank (message = "슬랙 Id는 필수입니다.")
    String SlackId,

    @NotNull(message = "hubId는 필수입니다.")
    UUID hubId,

    @NotNull(message = "DELIVERY_MANAGER, SUPPLIER_MANAGER 중 하나를 선택해야 합니다.")
    UserRole userRole

) {


    public DeliveryAminCommandV1 toDeliveryAminCommand() {
        return new DeliveryAminCommandV1(SlackId, hubId, userRole);
    }

}
