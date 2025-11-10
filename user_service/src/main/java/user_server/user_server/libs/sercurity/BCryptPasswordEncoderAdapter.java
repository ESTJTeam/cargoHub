package user_server.user_server.libs.sercurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import user_server.user_server.application.port.PasswordEncoder;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
