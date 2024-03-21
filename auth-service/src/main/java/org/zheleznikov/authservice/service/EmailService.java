package org.zheleznikov.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendEmail(Integer code, String email) {
        String emailSubject = "Your confirmation code";

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("polwen@yandex.ru");
        simpleMailMessage.setSubject(emailSubject);
        simpleMailMessage.setText(code.toString());

        emailSender.send(simpleMailMessage);
    }
}
