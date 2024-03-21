package org.zheleznikov.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zheleznikov.authservice.entity.UserCode;
import org.zheleznikov.authservice.entity.UserEntity;
import org.zheleznikov.authservice.exception.SignupException;
import org.zheleznikov.authservice.repository.UserCodeRepository;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ConfirmCodeService {

    private final UserCodeRepository userCodeRepository;

    private final EmailService emailService;

    /*
    !Непонятно, как это более правильно организовать.
    Надо сначала сохранить в БД, а потом отправить или наоборот
    Как быть если запись сохранится в БД, но сервис отправки не сработает.

    В нашем случае выглядит, как ничего страшного.
    Просто пользователь может попросить отправить ему код еще раз
     */
    public void sendCode(UserEntity user) {
        Integer code = generateCode();


        userCodeRepository.save(
                new UserCode()
                        .setUserId(user.getId())
                        .setIsConfirmed(user.getIsConfirmed())
                        .setCode(code)
                        .setExpiredTimestamp(LocalDateTime.now().plusMinutes(10))
                        .setCreatedTimestamp(LocalDateTime.now())
        );

        emailService.sendEmail(code, user.getEmail());

    }

    public void verifyCode(Integer code, UserEntity user) {
        /*
        ! По идее метод должен
        1. Сомнительная реализация
         */
        String exceptionMessage = "The code is incorrect or it has already been used or code expired";

        UserCode userCodeEntity = user.getUserCode()
                .stream()
                .filter(userCode -> LocalDateTime.now().isBefore(userCode.getExpiredTimestamp()))
                .filter(userCode -> userCode.getIsConfirmed().equals(false))
                .filter(userCode -> Objects.equals(userCode.getCode(), code))
                .findAny()
                .orElseThrow(() -> new SignupException(exceptionMessage));

       userCodeRepository.save(userCodeEntity.setIsConfirmed(true));
    }

    public void verifyCode2(Integer code, UserEntity user) {
        /*
        ! По идее метод должен
        1. Сомнительная реализация
         */
        String exceptionMessage = "The code is incorrect or it has already been used or code expired";

        UserCode userCodeEntity = user.getUserCode()
                .stream()
                .filter(userCode -> LocalDateTime.now().isBefore(userCode.getExpiredTimestamp()))
                .filter(userCode -> userCode.getIsConfirmed().equals(false))
                .filter(userCode -> Objects.equals(userCode.getCode(), code))
                .findAny()
                .orElseThrow(() -> new SignupException(exceptionMessage));

        userCodeRepository.save(userCodeEntity.setIsConfirmed(true));
    }

    private int generateCode() {
        int from = 1000;
        int to = 9999;
        return (int) ((Math.random() * (to - from)) + to);
    }
}