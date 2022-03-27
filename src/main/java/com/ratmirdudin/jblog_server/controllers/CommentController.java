package com.ratmirdudin.jblog_server.controllers;


import com.ratmirdudin.jblog_server.facades.CommentFacade;
import com.ratmirdudin.jblog_server.models.entities.Comment;
import com.ratmirdudin.jblog_server.payloads.requests.CommentRequest;
import com.ratmirdudin.jblog_server.payloads.responses.CommentRest;
import com.ratmirdudin.jblog_server.payloads.responses.Response;
import com.ratmirdudin.jblog_server.security.CurrentUser;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import com.ratmirdudin.jblog_server.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentFacade commentFacade;

    @PostMapping("/{postId}")
    public ResponseEntity<Response> createCommentForPost(@PathVariable Long postId,
                                                  @Valid @RequestBody CommentRequest commentRequest,
                                                  @CurrentUser UserPrincipal currentUser) {

        Comment comment = commentService.createComment(postId, commentRequest, currentUser);

        CommentRest commentRest = commentFacade.convertToRest(comment);

        HttpStatus status = CREATED;
        return ResponseEntity.status(status).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("comment", commentRest))
                        .message("Comment created")
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response> deleteCommentById(@PathVariable Long commentId,
                                               @CurrentUser UserPrincipal currentUser) {

        commentService.deleteCommentByiD(commentId, currentUser);

        HttpStatus status = NO_CONTENT;
        return ResponseEntity.status(status).body(
                Response.builder()
                        .timeStamp(now())
                        .message("Comment deleted by id")
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }

}

