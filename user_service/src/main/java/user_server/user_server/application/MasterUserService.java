package user_server.user_server.application;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_server.user_server.application.dto.command.SignupCommandV1;
import user_server.user_server.application.dto.command.SignupStatusCommandV1;
import user_server.user_server.application.dto.command.UpdateUserInfoCommandV1;
import user_server.user_server.application.dto.query.UserInfoQueryV1;
import user_server.user_server.application.dto.query.UserListQueryV1;
import user_server.user_server.infra.sercurity.dto.TokenBody;
import user_server.user_server.application.mapper.UserMapper;
import user_server.user_server.domain.entity.User;
import user_server.user_server.domain.repository.UserRepository;
import user_server.user_server.infra.sercurity.JwtTokenProvider;
import user_server.user_server.presentation.error.BusinessException;
import user_server.user_server.presentation.error.ErrorCode;
import user_server.user_server.presentation.success.dto.request.SignupStatusRequestV1;
import user_server.user_server.presentation.success.dto.request.UpdateUserInfoRequestV1;
import user_server.user_server.presentation.success.dto.request.UserSearchFilter;
import user_server.user_server.presentation.success.dto.response.UserInfoResponseV1;
import user_server.user_server.presentation.success.dto.response.UserListResponseV1;

@Service
@RequiredArgsConstructor
public class MasterUserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public List<UserListQueryV1> readAllUsers(String accessToken, UserSearchFilter filter, Pageable pageable) {

        // pageable로 바꿀 때 ddd는 인터페이스 어떻게 해야 하는지..? 일단 그냥 한번에 조회

        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        userRepository.findByUsername(tokenBody.getUsername()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        List<User> users;
        if (filter.equals(UserSearchFilter.PENDING)) {
            users = userRepository.findPendingUser();
        }
        else {
            users = userRepository.findAllUsers();
        }
        return UserMapper.toUserListQuery(users);
    }


    @Transactional
    public void updateStatus(String accessToken, UUID userId, SignupStatusCommandV1 request) {
        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        userRepository.findMasterUser(tokenBody.getUserId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.updateSignupStatus(request.signupStatus());
    }


    @Transactional(readOnly = true)
    public UserInfoQueryV1 readUser(String accessToken, UUID userId) {
        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        userRepository.findMasterUser(tokenBody.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toUserInfoQuery(user);
    }

    @Transactional
    public void updateUserInfo(String accessToken, UUID userId, UpdateUserInfoCommandV1 request) {

        TokenBody tokenBody = jwtTokenProvider.parseJwt(accessToken);
        userRepository.findMasterUser(tokenBody.getUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (request.slackId() != null && (!request.slackId().equals(user.getSlackId()))) {
            userRepository.findBySlackId(request.slackId()).ifPresent(u -> {
                throw new BusinessException(ErrorCode.DUPLICATE_SLACK_ID);
            });
            user.updateSlackId(request.slackId());
        }
        if (request.username() != null && !request.username().equals(user.getUsername())) {
            userRepository.findByUsername(request.username())
                .ifPresent(u -> {
                    throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
                });
            user.updateUsername(request.username());
        }

        if (request.nickname() != null && !request.nickname().equals(user.getNickname())) {
            userRepository.findByNickname(request.nickname()).ifPresent(u -> {
                throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
            });
            user.updateNickname(request.nickname());
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            userRepository.findByEmail(request.email()).ifPresent(u -> {
                throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
            });
            user.updateEmail(request.email());
        }

        if (request.role() != null && !request.role().equals(user.getRole())) {
            user.updateRole(request.role());
        }

        if (request.point() != null && !request.point().equals(user.getPoint())) {
            user.updatePoint(request.point());
        }

        if (request.signupStatus() != null && !request.signupStatus()
            .equals(user.getSignupStatus())) {
            user.updateSignupStatus(request.signupStatus());
        }
    }




}
