package user_server.user_server.application;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.dto.command.SignupStatusCommandV1;
import user_server.user_server.application.dto.command.UpdateUserInfoCommandV1;
import user_server.user_server.application.dto.query.UserInfoQueryV1;
import user_server.user_server.application.dto.query.UserListQueryV1;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;
import user_server.user_server.libs.sercurity.JwtTokenProvider;
import user_server.user_server.presentation.success.dto.request.UserSearchFilter;

@Service
@RequiredArgsConstructor
public class MasterUserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserValidationService userValidationService;

    @Transactional(readOnly = true)
    public List<UserListQueryV1> readAllUsers(String accessToken, UserSearchFilter filter, Pageable pageable) {
        userValidationService.validateUser(accessToken);

        List<User> users;
        if (filter.equals(UserSearchFilter.PENDING)) {
            users = userRepository.findPendingUser();
        }
        else {
            users = userRepository.findAllUsers();
        }
        return UserListQueryV1.fromUserListQuery(users);
    }


    @Transactional
    public void updateStatus(String accessToken, UUID userId, SignupStatusCommandV1 request) {
        User user = userValidationService.validateUserByMaster(accessToken, userId);
        userValidationService.validateSignupStatus(user, request.signupStatus());
    }


    @Transactional(readOnly = true)
    public UserInfoQueryV1 readUser(String accessToken, UUID userId) {
        User user = userValidationService.validateUserByMaster(accessToken, userId);
        return UserInfoQueryV1.fromUserInfoQuery(user);
    }


    @Transactional
    public void updateUserInfo(String accessToken, UUID userId, UpdateUserInfoCommandV1 request) {

        User user = userValidationService.validateUserByMaster(accessToken, userId);

        userValidationService.duplicateValidateSlackIdUpdate(user, request.slackId());
        userValidationService.duplicateValidateUsernameUpdate(user, request.username());
        userValidationService.duplicateValidateEmailUpdate(user, request.email());
        userValidationService.duplicateValidateNicknameUpdate(user, request.nickname());
        userValidationService.validateRoleUpdate(user, request.role());
        userValidationService.validatePointUpdate(user, request.point());
        userValidationService.validateSignupStatus(user, request.signupStatus());
    }




}
