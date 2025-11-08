package user_server.user_server.application;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user_server.user_server.domain.entity.Role;
import user_server.user_server.domain.entity.SignupStatus;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;
import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;
import user_server.user_server.libs.sercurity.JwtTokenProvider;
import user_server.user_server.libs.sercurity.dto.TokenBody;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public User validateUser(String accessToken) {
        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        return userRepository.findByUserId(tokenBody.getUserId()).orElseThrow(() -> new BusinessException(
            ErrorCode.USER_NOT_FOUND));
    }

    public User validateUserByMaster(String accessToken, UUID userId) {
        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        userRepository.findMasterUser(tokenBody.getUserId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }



    public void duplicateValidateSlackIdUpdate(User user, String slackId) {
        if (slackId != null && !slackId.equals(user.getSlackId())) {
            userRepository.findBySlackId(slackId).ifPresent(u -> { throw new BusinessException(ErrorCode.DUPLICATE_SLACK_ID); });
        }
        user.updateSlackId(user.getSlackId());
    }

    public void duplicateValidateUsernameUpdate(User user, String username) {
        if (username != null && !username.equals(user.getUsername())) {
            userRepository.findByUsername(username).ifPresent(u -> { throw new BusinessException(ErrorCode.DUPLICATE_USERNAME); });
        }
        user.updateUsername(user.getUsername());
    }

    public void duplicateValidateNicknameUpdate(User user, String nickname) {
        if (nickname != null && !nickname.equals(user.getNickname())) {
            userRepository.findByNickname(nickname).ifPresent(u -> { throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME); });
        }
        user.updateNickname(nickname);
    }

    public void duplicateValidateEmailUpdate(User user, String email) {
        if (email != null && !email.equals(user.getEmail())) {
            userRepository.findByEmail(email).ifPresent(u -> { throw new BusinessException(ErrorCode.DUPLICATE_EMAIL); });
        }
        user.updateEmail(email);
    }

    public void validateRoleUpdate(User user, Role role) {
        if (role != null && !role.equals(user.getRole())) {
            user.updateRole(role);
        }
    }

    public void validatePointUpdate(User user, Integer point) {
        if (point != null && !point.equals(user.getPoint())) {
            user.updatePoint(point);
        }
    }

    public void validateSignupStatus(User user, SignupStatus signupStatus) {
        if (signupStatus != null && !signupStatus.equals(user.getSignupStatus())) {
            user.updateSignupStatus(signupStatus);
        }
    }

}
