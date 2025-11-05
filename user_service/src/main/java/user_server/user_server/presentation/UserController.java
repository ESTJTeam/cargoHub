package user_server.user_server.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user_server.user_server.application.UserService;
import user_server.user_server.presentation.success.dto.BaseResponse;
import user_server.user_server.presentation.success.dto.BaseStatus;
import user_server.user_server.presentation.success.dto.request.LoginRequest;
import user_server.user_server.presentation.success.dto.request.SignupRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<Void> signup(@RequestBody @Valid SignupRequest signupRequest) {
        userService.create(signupRequest);
        return BaseResponse.ok(BaseStatus.CREATED);
    }


    @PostMapping("/login")
    public BaseResponse<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        userService.login(loginRequest, response);
        return BaseResponse.ok(BaseStatus.OK);
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") @NotBlank String accessToken, HttpServletResponse httpServletResponse) {
        userService.logout(httpServletResponse, accessToken);
        return BaseResponse.ok(BaseStatus.OK);
    }


    @GetMapping("/test")
    public String tset(){
        return "통과";
    }



}
