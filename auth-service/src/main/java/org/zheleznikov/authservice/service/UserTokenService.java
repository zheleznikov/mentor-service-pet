package org.zheleznikov.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zheleznikov.authservice.entity.UserToken;
import org.zheleznikov.authservice.exception.LogoutException;
import org.zheleznikov.authservice.repository.UserTokenRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final String jwtSecret = "secretKey";
    private final int jwtExpirationMs = 3600000;


    private final UserTokenRepository userTokenRepository;

    public UserToken saveToken(String email) {
        UserToken token = new UserToken()
                .setAccessToken(generateAccessToken(email))
                .setRefreshToken(generateRefreshToken())
                .setEmail(email);


        return userTokenRepository.save(token);
    }

    public void findUserByToken(String token) {
        UserToken user = userTokenRepository.findByAccessToken(token);

        if (user == null) {
            throw new LogoutException("No such access token");
        }

        userTokenRepository.delete(user);
    }

    private String generateAccessToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateRefreshToken() {
        return "refresh token";
    }
}
