package org.zheleznikov.authservice.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.zheleznikov.authservice.service.LoginUserService;
import org.zheleznikov.authservice.service.SignupUserService;
import org.zheleznikov.generated.model.ConfirmSignupRequest;
import org.zheleznikov.generated.model.LoginRequest;
import org.zheleznikov.generated.model.SignupRequest;
import org.zheleznikov.generated.model.SuccessCommonResponse;
import org.zheleznikov.generated.model.SuccessLoginResponse;
import org.zheleznikov.generated.model.SuccessSignupResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserControllerV1 {

    private final SignupUserService signupUserService;

    private final LoginUserService loginUserService;

    @PostMapping("/api/v1/user/signup")
    public SuccessSignupResponse signup(@RequestBody SignupRequest body) {

        return signupUserService.saveNewUser(body);
    }

    @PostMapping("/api/v1/user/signup-code")
    public SuccessCommonResponse askForCode(@RequestBody ConfirmSignupRequest body) {
        return signupUserService.sendCodeAgain(body);
    }

    @PostMapping("/api/v1/user/signup-confirmation")
    public SuccessLoginResponse confirmSignup(@RequestBody ConfirmSignupRequest body) {
        return signupUserService.confirmUserSignup(body);
    }

    @PostMapping("/api/v1/user/login")
    public SuccessLoginResponse login(@RequestBody LoginRequest body) {
        return loginUserService.loginUser(body);
    }


    @GetMapping("/api/v1/user/logout")
    public SuccessCommonResponse logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return loginUserService.logout(token);
    }
}
