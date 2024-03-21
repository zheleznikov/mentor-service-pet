package org.zheleznikov.authservice.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zheleznikov.authservice.exception.EmailConfirmException;
import org.zheleznikov.authservice.exception.LoginException;
import org.zheleznikov.authservice.exception.LogoutException;
import org.zheleznikov.authservice.exception.SignupException;
import org.zheleznikov.authservice.exception.SignupExceptionEmailAlreadyExists;
import org.zheleznikov.generated.model.ErrorCommonResponse;
import org.zheleznikov.generated.model.ErrorUserSignupResponse;

import java.time.OffsetDateTime;

@RestControllerAdvice(assignableTypes = {UserControllerV1.class})
@Slf4j
public class UserControllerV1ExceptionHandler {

    @ExceptionHandler(SignupExceptionEmailAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorUserSignupResponse handleEmailAlreadyExistsException(Exception e) {
        ErrorUserSignupResponse res = new ErrorUserSignupResponse();

        res.setMessage(e.getMessage());
        res.setStatus(409);
        res.setTimestamp(OffsetDateTime.now());

        return res;
    }

    @ExceptionHandler({SignupException.class, EmailConfirmException.class, LogoutException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorUserSignupResponse handleCommonException(Exception e) {
        ErrorUserSignupResponse res = new ErrorUserSignupResponse();

        res.setMessage(e.getMessage());
        res.setStatus(400);
        res.setTimestamp(OffsetDateTime.now());

        return res;
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorCommonResponse handleInvalidEmailOrPassword(Exception e) {
        ErrorCommonResponse res = new ErrorCommonResponse();

        res.setMessage(e.getMessage());
        res.setStatus(401);
        res.setTimestamp(OffsetDateTime.now());

        return res;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorCommonResponse handleAnyException(Exception e) {
        ErrorCommonResponse res = new ErrorCommonResponse();

        res.setMessage(e.getMessage());
        res.setStatus(500);
        res.setTimestamp(OffsetDateTime.now());

        return res;
    }


}
