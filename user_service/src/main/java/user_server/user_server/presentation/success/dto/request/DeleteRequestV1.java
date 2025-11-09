package user_server.user_server.presentation.success.dto.request;

import user_server.user_server.application.dto.command.DeleteCommandV1;

public record DeleteRequestV1(
    String username,
    String password
){
    public DeleteCommandV1 toCommand() {
        return new DeleteCommandV1(username, password);
    }

}

