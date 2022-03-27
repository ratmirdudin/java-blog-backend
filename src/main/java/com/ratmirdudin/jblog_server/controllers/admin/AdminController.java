package com.ratmirdudin.jblog_server.controllers.admin;

import com.ratmirdudin.jblog_server.facades.PostFacade;
import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.payloads.requests.PostRequest;
import com.ratmirdudin.jblog_server.payloads.responses.PostRest;
import com.ratmirdudin.jblog_server.payloads.responses.Response;
import com.ratmirdudin.jblog_server.security.CurrentUser;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import com.ratmirdudin.jblog_server.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;


@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final String POSTS_URL = "/posts";
    private final String USERS_URL = "/users";
    private final String COMMENTS_URL = "/comments";

    private final AdminService adminService;

    private final PostFacade postFacade;

    @PostMapping(POSTS_URL)
    public ResponseEntity<Response> createPost(@Valid @RequestBody PostRequest postRequest,
                                               @CurrentUser UserPrincipal currentUser) {

        Post post = adminService.createPost(postRequest, currentUser);

        PostRest postRest = postFacade.convertToPostRest(post);

        HttpStatus status = CREATED;
        return ResponseEntity.status(status).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("post", postRest))
                        .message("Post created")
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }

    @PostMapping(POSTS_URL + "/{postId}")
    public ResponseEntity<Response> updatePostById(@PathVariable Long postId,
                                                   @Valid @RequestBody PostRequest postRequest) {

        adminService.updatePostById(postId, postRequest);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message("Post updated by id")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping(POSTS_URL + "/{postId}")
    public ResponseEntity<Response> deletePostById(@PathVariable Long postId) {

        adminService.deletePostById(postId);

        HttpStatus status = NO_CONTENT;
        return ResponseEntity.status(status).body(
                Response.builder()
                        .timeStamp(now())
                        .message("Post deleted by id")
                        .status(status)
                        .statusCode(status.value())
                        .build()
        );
    }

    @GetMapping(USERS_URL + "/ban/{userId}")
    public ResponseEntity<Response> banUserById(@PathVariable Long userId) {

        adminService.banUserById(userId);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message("User banned by id")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping(USERS_URL + "/unban/{userId}")
    public ResponseEntity<?> unbanUserById(@PathVariable Long userId) {

        adminService.unbanUserById(userId);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message("User unbanned by id")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping(COMMENTS_URL + "/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId) {

        adminService.deleteCommentById(commentId);

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




