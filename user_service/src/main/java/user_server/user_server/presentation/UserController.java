package user_server.user_server.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import user_server.user_server.application.UserService;
import user_server.user_server.global.unit.common.BaseResponse;
import user_server.user_server.global.unit.common.BaseStatus;
import user_server.user_server.presentation.dto.request.LoginRequest;
import user_server.user_server.presentation.dto.request.SignupRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public BaseResponse<Void> signup(@RequestBody @Valid SignupRequest signupRequest) {
        userService.create(signupRequest);
        return BaseResponse.ok(BaseStatus.CREATED);
    }


    @PostMapping("/user/login")
    public BaseResponse<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        userService.login(loginRequest, response);
        return BaseResponse.ok(BaseStatus.OK);
    }


    @GetMapping("/user/test")
    public String tset(){
        return "통과";
    }



}
