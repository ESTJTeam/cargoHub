package user_server.user_server.application.dto.query;

import java.util.UUID;

public record UserListQueryV1 (
    UUID userId,
    String username
){}
