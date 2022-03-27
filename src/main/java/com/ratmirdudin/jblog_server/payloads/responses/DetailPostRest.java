package com.ratmirdudin.jblog_server.payloads.responses;

import lombok.Data;

import java.util.List;

@Data
public class DetailPostRest {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String createdDate;
    private String updatedDate;
    private List<CommentRest> comments;
}
