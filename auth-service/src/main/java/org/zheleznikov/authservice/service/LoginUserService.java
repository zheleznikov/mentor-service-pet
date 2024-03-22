package org.zheleznikov.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zheleznikov.authservice.entity.UserEntity;
import org.zheleznikov.authservice.entity.UserToken;
import org.zheleznikov.authservice.repository.UserRepository;
import org.zheleznikov.generated.model.LoginRequest;
import org.zheleznikov.generated.model.SuccessCommonResponse;
import org.zheleznikov.generated.model.SuccessLoginResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.zheleznikov.authservice.service.DtoService.prepareStatus;
import static org.zheleznikov.authservice.service.DtoService.prepareToken;
import static org.zheleznikov.authservice.service.DtoService.prepareUserInfo;
import static org.zheleznikov.authservice.service.ValidationService.validateIfPasswordCorrect;
import static org.zheleznikov.authservice.service.ValidationService.validateIfUserExists;

@Service
@RequiredArgsConstructor
// для логина и логаута сделал один сервис. а для сиг
public class LoginUserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserTokenService userTokenService;

    public SuccessLoginResponse loginUser(LoginRequest request) {

        // надо ли вообще оборачивать сохранение в постгрес и редис в try-catcn
        try {
            UserEntity userEntity = userRepository.findByEmail(request.getEmail());

            validateIfUserExists(userEntity);
            validateIfPasswordCorrect(request, userEntity, bCryptPasswordEncoder);

            LocalDateTime now = LocalDateTime.now();

            userRepository.save(userEntity.setLastLoginTimestamp(now));

            UserToken token = userTokenService.saveToken(userEntity.getEmail());

            // создание ответа это не очень красиво выглядит, занимает много места
            SuccessLoginResponse response = new SuccessLoginResponse();
            response.setMessage("User successfully login");
            response.setStatus(200);
            response.setTimestamp(now.atOffset(OffsetDateTime.now().getOffset()));

            response.setSignupStatus(prepareStatus(userEntity));
            response.setUserInfo(prepareUserInfo(userEntity));
            response.setTokenInfo(prepareToken(token));

            return response;
        } catch (RuntimeException exception) {
            throw new RuntimeException(exception.getMessage());
        }

    }

    public SuccessCommonResponse logout(String accessToken) {
        SuccessCommonResponse response = new SuccessCommonResponse();


        userTokenService.findUserByToken(accessToken);

        response.setMessage("Logout success");
        response.setStatus(204);
        response.setTimestamp(OffsetDateTime.now());

        return response;
    }
}
