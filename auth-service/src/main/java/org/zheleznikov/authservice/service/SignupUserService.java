package org.zheleznikov.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zheleznikov.authservice.entity.UserEntity;
import org.zheleznikov.authservice.entity.UserToken;
import org.zheleznikov.authservice.repository.UserRepository;
import org.zheleznikov.generated.model.ConfirmSignupRequest;
import org.zheleznikov.generated.model.Role;
import org.zheleznikov.generated.model.SignupRequest;
import org.zheleznikov.generated.model.SuccessCommonResponse;
import org.zheleznikov.generated.model.SuccessLoginResponse;
import org.zheleznikov.generated.model.SuccessSignupResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.zheleznikov.authservice.service.DtoService.prepareStatus;
import static org.zheleznikov.authservice.service.DtoService.prepareToken;
import static org.zheleznikov.authservice.service.DtoService.prepareUserInfo;
import static org.zheleznikov.authservice.service.ValidationService.validateIfConfirmationCodeCreated;
import static org.zheleznikov.authservice.service.ValidationService.validateIfEmailAlreadyExists;
import static org.zheleznikov.authservice.service.ValidationService.validateIfPasswordsDoNotMatch;
import static org.zheleznikov.authservice.service.ValidationService.validateIfPossibleToSendCode;
import static org.zheleznikov.authservice.service.ValidationService.validateIfUserExists;

@Service
@RequiredArgsConstructor
public class SignupUserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmCodeService confirmCodeService;

    private final UserTokenService userTokenService;


    public SuccessSignupResponse saveNewUser(SignupRequest request) {
        validateIfPasswordsDoNotMatch(request);

        try {
            validateIfEmailAlreadyExists(request, userRepository);

            LocalDateTime now = LocalDateTime.now();


            UserEntity userEntity = new UserEntity()
                    .setSignupTimestamp(now)
                    .setName(request.getName())
                    .setEmail(request.getEmail())
                    .setRole(Role.USER_MENTY)
                    .setIsConfirmed(false)
                    .setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

            UserEntity saved = userRepository.save(userEntity);

            confirmCodeService.sendCode(saved);


            String successMessage = "Verification code send on email: " + userEntity.getEmail();

            SuccessSignupResponse response = new SuccessSignupResponse();
            response.setMessage(successMessage);
            response.setTimestamp(now.atOffset(OffsetDateTime.now().getOffset()));
            response.setStatus(201);

            response.setSignupStatus(prepareStatus(userEntity));
            response.setUserInfo(prepareUserInfo(userEntity));

            return response;

        } catch (Exception e) {
            throw new RuntimeException();
        }

    }


    public SuccessLoginResponse confirmUserSignup(ConfirmSignupRequest request) {
        // надо ли оборачивать это все в try-catch
        try {
            UserEntity userEntity = userRepository.findByEmail(request.getEmail());

            validateIfUserExists(userEntity);
            validateIfConfirmationCodeCreated(userEntity);

            confirmCodeService.verifyCode(request.getCode(), userEntity);

            LocalDateTime now = LocalDateTime.now();

            userRepository.save(userEntity
                    .setConfirmTimestamp(now)
                    .setIsConfirmed(true));

            UserToken userToken = userTokenService.saveToken(userEntity.getEmail());

            SuccessLoginResponse response = new SuccessLoginResponse();
            response.setMessage("Email successfully confirmed. User login");
            response.setStatus(200);
            response.setTimestamp(now.atOffset(OffsetDateTime.now().getOffset()));

            response.setSignupStatus(prepareStatus(userEntity));
            response.setUserInfo(prepareUserInfo(userEntity));
            response.setTokenInfo(prepareToken(userToken));

            return response;

        } catch (RuntimeException exception) {
            throw new RuntimeException(exception.getMessage());
        }

    }


    public SuccessCommonResponse sendCodeAgain(ConfirmSignupRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail());

        validateIfPossibleToSendCode(user);

        confirmCodeService.sendCode(user);

        LocalDateTime now = LocalDateTime.now();

        SuccessCommonResponse response = new SuccessCommonResponse();
        response.setTimestamp(now.atOffset(OffsetDateTime.now().getOffset()));
        response.setStatus(200);
        response.setMessage("Code send to email " + request.getEmail());

        return response;
    }
}
