package com.ratmirdudin.jblog_server.payloads.responses;

import lombok.Data;

import java.util.Map;

@Data
public class CommentRest {
    private Long id;
    private String message;
    private Map<String, Object> user;
    private String createdDate;
    private String updatedDate;
}
