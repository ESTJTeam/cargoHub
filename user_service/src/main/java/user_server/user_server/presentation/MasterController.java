package user_server.user_server.presentation;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import user_server.user_server.application.MasterUserService;
import user_server.user_server.application.dto.command.SignupStatusCommandV1;
import user_server.user_server.application.dto.command.UpdateUserInfoCommandV1;
import user_server.user_server.application.dto.query.UserInfoQueryV1;
import user_server.user_server.application.dto.query.UserListQueryV1;
import user_server.user_server.application.mapper.UserMapper;
import user_server.user_server.presentation.success.dto.BaseResponse;
import user_server.user_server.presentation.success.dto.BaseStatus;
import user_server.user_server.presentation.success.dto.request.SignupStatusRequestV1;
import user_server.user_server.presentation.success.dto.request.UserSearchFilter;
import user_server.user_server.presentation.success.dto.request.UpdateUserInfoRequestV1;
import user_server.user_server.presentation.success.dto.response.UserInfoResponseV1;
import user_server.user_server.presentation.success.dto.response.UserListResponseV1;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/master")
public class MasterController {

    private final MasterUserService masterUserService;

    // 전체 유저 조회로 pending 상태인 유저만 조회도 가능 (REJECTED도 가능)
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<UserListResponseV1>> readAllUsers(
        @RequestHeader("Authorization") String accessToken,
        @RequestParam(defaultValue = "ALL") UserSearchFilter userSearchFilter,
        @PageableDefault(page = 0, size = 10, sort = "createAt", direction = Direction.DESC) Pageable pageable) {
        List<UserListQueryV1> userListQuery = masterUserService.readAllUsers(accessToken, userSearchFilter, pageable);
        List<UserListResponseV1> response = UserMapper.toUserListResponse(userListQuery);
        return BaseResponse.ok(response, BaseStatus.OK);
    }


    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserInfoResponseV1> readOneUsers(
        @RequestHeader("Authorization") @NotBlank String accessToken, @PathVariable UUID userId) {
        UserInfoQueryV1 userInfoQuery = masterUserService.readUser(accessToken, userId);
        UserInfoResponseV1 response = UserMapper.toUserInfoResponse(userInfoQuery);
        return BaseResponse.ok(response, BaseStatus.OK);
    }


    @PostMapping("/users/{userId}/status")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateStatus(
        @RequestHeader("Authorization") @NotBlank String accessToken, @PathVariable UUID userId, @RequestBody SignupStatusRequestV1 request) {
        SignupStatusCommandV1 signupStatusCommand = new SignupStatusCommandV1(request.signupStatus());
        masterUserService.updateStatus(accessToken, userId, signupStatusCommand);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @PatchMapping("/users/{userId}/edit")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateUserInfo(@RequestHeader("Authorization") @NotBlank String accessToken,
        @PathVariable("userId") UUID userId, @RequestBody UpdateUserInfoRequestV1 request) {
        UpdateUserInfoCommandV1 updateUserInfoCommand = UserMapper.toUpdateUserInfoCommandV1(request);
        masterUserService.updateUserInfo(accessToken,userId, updateUserInfoCommand);
        return BaseResponse.ok(BaseStatus.OK);
    }


    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test(){
        return "통과";
    }

}
