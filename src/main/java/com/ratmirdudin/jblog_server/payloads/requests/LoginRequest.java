package com.ratmirdudin.jblog_server.payloads.requests;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class LoginRequest {

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;
}
