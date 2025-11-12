package user_server.user_server.application.dto.command;

import java.util.UUID;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.vo.UserRole;

public record DeliveryAminCommandV1 (
    String SlackId,
    UUID hubId,
    UserRole userRole
){}
