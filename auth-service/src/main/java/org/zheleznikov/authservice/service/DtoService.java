package org.zheleznikov.authservice.service;

import org.zheleznikov.authservice.entity.UserEntity;
import org.zheleznikov.authservice.entity.UserToken;
import org.zheleznikov.generated.model.SignupStatus;
import org.zheleznikov.generated.model.Token;
import org.zheleznikov.generated.model.User;

/**
 * Это сервис, пока я не придумал ничего лучше, как быстро формировать DTO из open api
 * без использования @accessors(chain = true)
 */
public class DtoService {

    public static Token prepareToken() {
        Token token = new Token();

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);

        return token;
    }

    public static Token prepareToken(UserToken tokenData) {
        Token token = new Token();
        token.setAccessToken(tokenData.getAccessToken());
        token.setRefreshToken(tokenData.getRefreshToken());

        return token;
    }

    public static SignupStatus prepareStatus(UserEntity userEntity) {
        SignupStatus status = new SignupStatus();
        status.setRegistered(true);
        status.setConfirmed(userEntity.getIsConfirmed());

        return status;
    }

    public static User prepareUserInfo(UserEntity userEntity) {
        User userRes = new User();
        userRes.setEmail(userEntity.getEmail());
        userRes.setName(userEntity.getName());
        userRes.setRole(userEntity.getRole());

        return userRes;
    }
}
