package com.foodtech.blog.base.service;

import com.foodtech.blog.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    @Value("spring.mail.username")
    private String fromMail;

    public void sendEmail(String title, String body, String to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);
    }

    public void sendEmailRegistration(String to) {
        sendEmail("Вы зарегестрировались!", "Вы успешно зарегестрировались", to);
    }

    public void sendEmailAlert(String to) {
        String ip = AuthService.getCurrentHttpRequest().getRemoteAddr();
        sendEmail("Внимание! неудачная попытка входа!", "Неудачная попытка входа с IP " + ip, to);
    }
}
