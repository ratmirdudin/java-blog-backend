package com.ratmirdudin.jblog_server.payloads.requests;

import com.ratmirdudin.jblog_server.annotations.MyEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Size(min = 3, max = 15, message = "Length should be between 3 and 15 symbols")
    @NotBlank(message = "Username is required")
    private String username;

    @MyEmail
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, max = 20, message = "Length should be between 6 and 20 symbols")
    @NotBlank(message = "Password is required")
    private String password;

    @Size(max = 15, message = "Length should be between 0 and 15 symbols")
    private String firstname;

    @Size(max = 15, message = "Length should be between 0 and 15 symbols")
    private String lastname;
}
