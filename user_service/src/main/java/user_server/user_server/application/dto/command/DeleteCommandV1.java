package user_server.user_server.application.dto.command;

public record DeleteCommandV1 (
    String username,
    String password)
{}
