package user_server.user_server.application;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.dto.command.DeleteCommandV1;
import user_server.user_server.application.dto.command.LoginCommandV1;
import user_server.user_server.application.dto.command.SignupCommandV1;
import user_server.user_server.application.dto.command.UpdateMyInfoCommandV1;
import user_server.user_server.application.dto.query.MyInfoQueryV1;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;
import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;
import user_server.user_server.libs.sercurity.BCryptPasswordEncoderAdapter;
import user_server.user_server.libs.sercurity.JwtTokenProvider;
import user_server.user_server.libs.sercurity.dto.TokenBody;
import user_server.user_server.libs.unit.CookieUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoderAdapter passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void create(SignupCommandV1 signupRequest) {
        userRepository.findByUsernameAndEmail(signupRequest.username(), signupRequest.email()).ifPresent(finduser -> {
                throw new BusinessException(ErrorCode.DUPLICATE_USER);});
        String password = passwordEncoder.encode(signupRequest.password());
        User user = User.createUser(signupRequest.slackId(), password, signupRequest.username(),
            signupRequest.role(), signupRequest.nickname(), signupRequest.email());
        userRepository.save(user);
    }


    @Transactional
    public void login(LoginCommandV1 loginCommand, HttpServletResponse response) {
        User user = userRepository.findByUsername(loginCommand.username()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.isLoginAllowed(user)){
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if (!passwordEncoder.matches(loginCommand.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        String accessToken  = jwtTokenProvider.issueAccessToken(user.getId(), user.getRole(), user.getUsername());
        String refreshToken = jwtTokenProvider.issueRefreshToken(user.getId(), user.getRole(), user.getUsername());
        Duration ttl = ttlTime(refreshToken);
        user.updateRefreshToken(refreshToken, LocalDateTime.now().plus(ttl));

        response.setHeader("Authorization", "Bearer " + accessToken);
        CookieUtils.setRefreshTokenCookie(response, refreshToken, ttl);

        // TODO redis 예정
    }


    @Transactional
    public void logout(HttpServletResponse response, String accessToken) {
        User user = userValidationService.validateUser(accessToken);
        user.clearRefreshToken();
        CookieUtils.deleteRefreshTokenCookie(response);
    }

    @Transactional
    public void delete(HttpServletResponse response, String accessToken, DeleteCommandV1 deleteRequest) {
        User user = userValidationService.validateUser(accessToken);

        if (! passwordEncoder.matches(deleteRequest.password(), user.getPassword())){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.clearRefreshToken();
        user.delete(user.getId());
        CookieUtils.deleteRefreshTokenCookie(response);
    }

    @Transactional
    public void reissue(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);}
        TokenBody tokenBody = jwtTokenProvider.parseJwt(refreshToken);
        User user = userRepository.findPublicById(tokenBody.getUserId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!refreshToken.equals(user.getRefreshToken())) {throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);}

        // 토큰 재발급
        String newAccessToken  = jwtTokenProvider.issueAccessToken(user.getId(), user.getRole(), user.getUsername());
        String newRefreshToken = jwtTokenProvider.issueRefreshToken(user.getId(), user.getRole(), user.getUsername());
        Duration ttl = ttlTime(newRefreshToken);
        user.updateRefreshToken(newRefreshToken, LocalDateTime.now().plus(ttl));
        userRepository.save(user);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        CookieUtils.setRefreshTokenCookie(response, newRefreshToken, ttl);
    }

    @Transactional(readOnly = true)
        public MyInfoQueryV1 readMyInfo(String accessToken) {
        User user = userValidationService.validateUser(accessToken);
        return MyInfoQueryV1.fromMyInfoQuery(user);
    }

    @Transactional
    public void updateMyInfo( String accessToken, UpdateMyInfoCommandV1 request) {
        User user = userValidationService.validateUser(accessToken);

        userValidationService.duplicateValidateSlackIdUpdate(user, request.slackId());
        userValidationService.duplicateValidateUsernameUpdate(user, request.username());
        userValidationService.duplicateValidateEmailUpdate(user, request.email());
        userValidationService.duplicateValidateNicknameUpdate(user, request.nickname());
        userValidationService.validateRoleUpdate(user, request.role());
    }


    private Duration ttlTime(String jwt) {
        long millis = jwtTokenProvider.getExpiration(jwt).getTime() - System.currentTimeMillis();
        return millis <= 0 ? Duration.ZERO : Duration.ofMillis(millis);
    }

}


