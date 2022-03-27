package com.ratmirdudin.jblog_server.payloads.requests;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class CommentRequest {
    @NotEmpty(message = "Message is required")
    private String message;
}
