package user_server.user_server.application.port;

public interface PasswordEncoder {
    String encode(String raw);
    boolean matches(String raw, String encoded);
}