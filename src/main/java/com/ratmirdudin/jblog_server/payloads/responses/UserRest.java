package com.ratmirdudin.jblog_server.payloads.responses;

import lombok.Data;

@Data
public class UserRest {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private boolean enabled;
}
