package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.controllers.AuthController;
import com.ratmirdudin.jblog_server.models.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    public final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendVerificationCodeTo(User user) {
        log.info("Sending verification code: \"{}\" to user with email: \"{}\"", user.getVerificationCode(), user.getEmail());
        try {
            String link = linkTo(methodOn(AuthController.class).verifyUserAccount(user.getVerificationCode())).toUri().toString();
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String username = user.getUsername();
            String htmlMsg = "Здравствуйте, <b>" + username + "!</b> Спасибо за регистрацию на ресурсе Java Blog.\n " + "<a href='" + link + "'>Пожалуйста, перейдите по ссылке для активации аккаунта.</a>";
            message.setContent(htmlMsg, "text/html; charset=utf-8");
            helper.setFrom(mailFrom);
            helper.setTo(user.getEmail());
            helper.setSubject("Активация аккаунта");
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
