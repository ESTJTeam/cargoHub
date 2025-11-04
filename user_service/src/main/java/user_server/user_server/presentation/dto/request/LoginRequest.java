package user_server.user_server.presentation.dto.request;

public record LoginRequest (
    String slackId,
    String password
){}
