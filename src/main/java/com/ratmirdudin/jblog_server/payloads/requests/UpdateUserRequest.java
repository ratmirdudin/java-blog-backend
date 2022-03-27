package com.ratmirdudin.jblog_server.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(max = 15, message = "Length should be between 0 and 15 symbols")
    private String firstName;

    @Size(max = 15, message = "Length should be between 0 and 15 symbols")
    private String lastName;

}
