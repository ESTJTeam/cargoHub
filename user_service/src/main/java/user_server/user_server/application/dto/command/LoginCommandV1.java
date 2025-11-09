package user_server.user_server.application.dto.command;

public record LoginCommandV1 (
    String username,
    String password
){
    public LoginCommandV1 toLoginCommand(String username, String password) {
        return new LoginCommandV1(username, password);
    }

}