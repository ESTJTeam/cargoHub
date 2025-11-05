package user_server.user_server.application;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.dto.TokenBody;
import user_server.user_server.application.mapper.UserMapper;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;
import user_server.user_server.presentation.error.BusinessException;
import user_server.user_server.presentation.error.ErrorCode;
import user_server.user_server.presentation.unit.CookieUtils;
import user_server.user_server.infra.sercurity.BCryptPasswordEncoderAdapter;
import user_server.user_server.infra.sercurity.JwtTokenProvider;
import user_server.user_server.presentation.success.dto.request.LoginRequest;
import user_server.user_server.presentation.success.dto.request.SignupRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${custom.jwt.exp-time.access}")
    private long accessTokenTime;

    @Value("${custom.jwt.exp-time.refresh}")
    private long refreshTokenTime;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoderAdapter passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void create(SignupRequest signupRequest) {
        userRepository.findByUsername(signupRequest.username()).ifPresent(finduser -> {
                throw new BusinessException(ErrorCode.ID_ALREADY_EXISTS);
            });
        String password = passwordEncoder.encode(signupRequest.password());
        User user = UserMapper.toUser( signupRequest.slackId(), password, signupRequest.username(), signupRequest.role(),
            signupRequest.nickname(), signupRequest.email());
        userRepository.save(user);
    }


    @Transactional
    public void login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.isLoginAllowed(user)){
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        String accessToken  = jwtTokenProvider.issueAccessToken(user.getId(), user.getRole(), user.getUsername());
        String refreshToken = jwtTokenProvider.issueRefreshToken(user.getId(), user.getRole(), user.getUsername());
        Duration ttl = ttlTime(refreshToken);
        user.updateRefreshToken(refreshToken, LocalDateTime.now().plus(ttl));
        userRepository.save(user);

        response.setHeader("Authorization", "Bearer " + accessToken);
        CookieUtils.setRefreshTokenCookie(response, refreshToken, ttl);

        // TODO redis 예정
    }


    @Transactional
    public void logout(HttpServletResponse response, String accessToken) {
        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        User user = userRepository.findPublicById(tokenBody.getUserId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (user.isRefreshExpired(user.getRefreshTokenExpiresAt())) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        user.clearRefreshToken();
        CookieUtils.deleteRefreshTokenCookie(response);
    }






    private Duration ttlTime(String jwt) {
        long millis = jwtTokenProvider.getExpiration(jwt).getTime() - System.currentTimeMillis();
        return millis <= 0 ? Duration.ZERO : Duration.ofMillis(millis);
    }

}


