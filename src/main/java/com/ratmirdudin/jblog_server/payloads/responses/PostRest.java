package com.ratmirdudin.jblog_server.payloads.responses;

import lombok.Data;

@Data
public class PostRest {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String createdDate;
    private String updatedDate;
}
