package com.ratmirdudin.jblog_server.payloads.requests;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
}
