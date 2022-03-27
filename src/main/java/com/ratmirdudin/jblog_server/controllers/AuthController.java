package com.ratmirdudin.jblog_server.controllers;


import com.ratmirdudin.jblog_server.facades.UserFacade;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.payloads.requests.LoginRequest;
import com.ratmirdudin.jblog_server.payloads.requests.SignUpRequest;
import com.ratmirdudin.jblog_server.payloads.responses.Response;
import com.ratmirdudin.jblog_server.payloads.responses.UserRest;
import com.ratmirdudin.jblog_server.services.AuthService;
import com.ratmirdudin.jblog_server.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;
    private final UserFacade userFacade;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @PostMapping("/signin")
    public ResponseEntity<Response> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String jwt = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("accessToken", jwt))
                        .message("User authorized")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        User user = authService.createUser(signUpRequest);

        UserRest userRest = userFacade.convertToRest(user);

        if (activeProfile.equals("prod")) {
            mailService.sendVerificationCodeTo(user);
        }

        HttpStatus status = CREATED;
        return ResponseEntity.status(status).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userRest))
                        .message("User registered")
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }

    @GetMapping("/verify/{verificationCode}")
    public RedirectView verifyUserAccount(@PathVariable String verificationCode) {

        authService.verifyUserAccount(verificationCode);

        return new RedirectView("https://biz.mail.ru/login/rv-tech.online");
    }

}

