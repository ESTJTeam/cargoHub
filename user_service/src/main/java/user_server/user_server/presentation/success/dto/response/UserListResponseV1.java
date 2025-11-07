package user_server.user_server.presentation.success.dto.response;

import java.util.UUID;

public record UserListResponseV1(
    UUID userId,
    String username) {}
