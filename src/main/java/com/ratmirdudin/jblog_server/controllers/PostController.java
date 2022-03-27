package com.ratmirdudin.jblog_server.controllers;


import com.ratmirdudin.jblog_server.facades.PostFacade;
import com.ratmirdudin.jblog_server.models.entities.Post;
import com.ratmirdudin.jblog_server.payloads.responses.DetailPostRest;
import com.ratmirdudin.jblog_server.payloads.responses.PageMetaData;
import com.ratmirdudin.jblog_server.payloads.responses.PostRest;
import com.ratmirdudin.jblog_server.payloads.responses.Response;
import com.ratmirdudin.jblog_server.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostFacade postFacade;


    @GetMapping
    public ResponseEntity<Response> getAllPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "50") int limit,
                                         @RequestParam(value = "category", defaultValue = "all") String category,
                                         @RequestParam(value = "search", defaultValue = "") String search) {

        Page<Post> postPage = postService.getAllPosts(search, category, page, limit);

        List<PostRest> postRestList = postPage.getContent()
                .stream()
                .map(postFacade::convertToPostRest)
                .collect(Collectors.toList());

        int lastPage = postPage.getTotalPages();
        int currentPage = postPage.getNumber() + 1;
        long totalElements = postPage.getTotalElements();

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("posts", postRestList,
                                "page", new PageMetaData(currentPage, lastPage, totalElements)
                        ))
                        .message("Posts received")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Response> getDetailPostById(@PathVariable Long postId) {

        Post post = postService.getDetailPostById(postId);

        DetailPostRest detailPostRest = postFacade.convertToDetailPostRest(post);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("detailPost", detailPostRest))
                        .message("Detail post received by id")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}

