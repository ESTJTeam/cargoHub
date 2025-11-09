package user_server.user_server.presentation.success.dto.request;

import user_server.user_server.application.dto.command.LoginCommandV1;

public record LoginRequestV1(
    String username,
    String password
){
    public LoginCommandV1 toLoginCommand() {
        return new LoginCommandV1(username, password);
    }
}
