package user_server.user_server.infra.external.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateDeliveryAdminRequestV1 (

    @NotBlank(message = "슬랙 Id는 필수입니다.")
    String SlackId,

    @NotNull(message = "hubId는 필수입니다.")
    UUID hubId,

    @NotNull(message = "DELIVERY_MANAGER, SUPPLIER_MANAGER 중 하나를 선택해야 합니다.")
    String userRole
){}