package user_server.user_server.presentation.success.dto.request;

public record LoginRequest (
    String username,
    String password
){}
