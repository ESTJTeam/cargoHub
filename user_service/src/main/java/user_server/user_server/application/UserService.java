package user_server.user_server.application;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.mapper.UserMapper;
import user_server.user_server.domain.entity.SignupStatus;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;
import user_server.user_server.domain.service.UserDomainService;
import user_server.user_server.global.exception.BusinessException;
import user_server.user_server.global.exception.domain.ErrorCode;
import user_server.user_server.global.unit.CookieUtils;
import user_server.user_server.infra.sercurity.BCryptPasswordEncoderAdapter;
import user_server.user_server.infra.sercurity.JwtTokenProvider;
import user_server.user_server.presentation.dto.request.LoginRequest;
import user_server.user_server.presentation.dto.request.SignupRequest;

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
        userRepository.findBySlackId(signupRequest.slackId()).ifPresent(finduser -> {
                throw new BusinessException(ErrorCode.SLACK_ID_ALREADY_EXISTS);
            });
        String password = passwordEncoder.encode(signupRequest.password());
        User user = UserMapper.toUser(signupRequest.name(), password, signupRequest.role(), signupRequest.slackId());
        userRepository.save(user);
    }


    @Transactional // 리프래시 토큰이 어케 되는지에 따라 읽기가 될지?
    public void login(@Valid LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findBySlackId(loginRequest.slackId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getSignupStatus().equals(SignupStatus.PENDING) || user.getSignupStatus().equals(SignupStatus.REJECTED) ) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        String accessToken  = jwtTokenProvider.issueAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.issueRefreshToken(user.getId(), user.getRole());
        Duration ttl = ttlTime(refreshToken);
        user.updateRefreshToken(refreshToken, LocalDateTime.now().plus(ttl));
        userRepository.save(user);

        response.setHeader("Authorization", "Bearer " + accessToken);
        CookieUtils.setRefreshTokenCookie(response, refreshToken, ttl);

        // TODO redis 예정
    }




    private Duration ttlTime(String jwt) {
        long millis = jwtTokenProvider.getExpiration(jwt).getTime() - System.currentTimeMillis();
        return millis <= 0 ? Duration.ZERO : Duration.ofMillis(millis);
    }
}


