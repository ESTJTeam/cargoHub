package cargohub.orchestratorservice.application.dto;

import cargohub.orchestratorservice.domain.UserRole;
import java.util.UUID;

public record UserInfoResponse(
        UUID userId,
        UserRole role,
        String username,
        String type
) {
    public static UserInfoResponse anonymous() {
        return new UserInfoResponse(
                null,
                null,
                "anonymous",
                "anonymous"
        );
    }
}
