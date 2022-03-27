package com.ratmirdudin.jblog_server.controllers;


import com.ratmirdudin.jblog_server.facades.UserFacade;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.payloads.requests.UpdateUserRequest;
import com.ratmirdudin.jblog_server.payloads.responses.PageMetaData;
import com.ratmirdudin.jblog_server.payloads.responses.Response;
import com.ratmirdudin.jblog_server.payloads.responses.UserRest;
import com.ratmirdudin.jblog_server.security.CurrentUser;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import com.ratmirdudin.jblog_server.services.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;

    @GetMapping
    public ResponseEntity<Response> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "50") int limit,
                                         @RequestParam(value = "search", defaultValue = "") String search) {

        Page<User> userPage = userService.getAllUsers(search, page, limit);

        List<UserRest> userRestList = userPage.getContent()
                .stream()
                .map(userFacade::convertToRest)
                .collect(Collectors.toList());

        int lastPage = userPage.getTotalPages();
        int currentPage = userPage.getNumber() + 1;
        long totalElements = userPage.getTotalElements();

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("users", userRestList,
                                "page", new PageMetaData(currentPage, lastPage, totalElements)
                        ))
                        .message("Users received")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );

    }

    @PutMapping
    public ResponseEntity<Response> updateUserInfo(@RequestBody UpdateUserRequest updateUserRequest,
                                            @CurrentUser UserPrincipal currentUser) {

        userService.updateUserById(updateUserRequest, currentUser);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message("User updated his info")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Response> getCurrentUser(@CurrentUser UserPrincipal currentUser) {

        User user = userService.getUserById(currentUser.getId());

        UserRest userRest = userFacade.convertToRest(user);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userRest))
                        .message("Current user received by id")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable Long userId) {

        User user = userService.getUserById(userId);

        UserRest userRest = userFacade.convertToRest(user);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userRest))
                        .message("User received by id")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}

