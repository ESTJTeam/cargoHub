package user_server.user_server.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import user_server.user_server.application.UserService;
import user_server.user_server.infra.external.dto.response.UserResponseV1;
import user_server.user_server.presentation.success.dto.BaseResponse;
import user_server.user_server.presentation.success.dto.BaseStatus;
import user_server.user_server.presentation.success.dto.request.DeleteRequestV1;
import user_server.user_server.presentation.success.dto.request.LoginRequestV1;
import user_server.user_server.presentation.success.dto.request.SignupRequestV1;
import user_server.user_server.presentation.success.dto.request.UpdateMyInfoRequestV1;
import user_server.user_server.presentation.success.dto.response.MyInfoResponseV1;
import user_server.user_server.presentation.success.dto.response.UserInfoResponseV1;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> signup(@RequestBody @Valid SignupRequestV1 signupRequest) {
        userService.create(signupRequest.toSignupCommandV1());
        return BaseResponse.ok(BaseStatus.CREATED);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> login(@RequestBody @Valid LoginRequestV1 loginRequest,
        HttpServletResponse response) {
        userService.login(loginRequest.toLoginCommand(), response);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> logout(@RequestHeader("Authorization") @NotBlank String accessToken,
        HttpServletResponse httpServletResponse) {
        userService.logout(httpServletResponse, accessToken);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> delete(HttpServletResponse httpServletResponse,
        @RequestHeader("Authorization") @NotBlank String accessToken,
        @RequestBody @Valid DeleteRequestV1 deleteRequest) {
        userService.delete(httpServletResponse, accessToken, deleteRequest.toCommand());
        return BaseResponse.ok(BaseStatus.OK);
    }


    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> reissue(
        @CookieValue(value = "refreshToken", required = false) String refreshToken,
        HttpServletResponse response) {
        userService.reissue(refreshToken, response);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @GetMapping("/myinfo")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<MyInfoResponseV1> readMyInfo(
        @RequestHeader("Authorization") @NotBlank String accessToken) {
        return BaseResponse.ok(
            MyInfoResponseV1.fromUserInfoResponse(userService.readMyInfo(accessToken)),
            BaseStatus.OK);
    }

    @PatchMapping("/myinfo")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> updateMyInfo(
        @RequestHeader("Authorization") @NotBlank String accessToken,
        @RequestBody UpdateMyInfoRequestV1 updateMyInfoRequest) {
        userService.updateMyInfo(accessToken, updateMyInfoRequest.toUpdateMyInfoCommandV1());
        return BaseResponse.ok(BaseStatus.OK);
    }


}
