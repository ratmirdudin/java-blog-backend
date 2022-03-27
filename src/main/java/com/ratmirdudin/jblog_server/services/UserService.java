package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.exceptions.BadRequestException;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.payloads.requests.UpdateUserRequest;
import com.ratmirdudin.jblog_server.repositories.UserRepository;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<User> getAllUsers(String search, int page, int limit) {
        log.info("Getting all users with params search: \"{}\", page: \"{}\", limit: \"{}\"",
                search, page, limit
        );
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 50;
        }
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageableRequest = PageRequest.of(page - 1, limit, sort);
        return userRepository.findAllUsers(search, pageableRequest);
    }

    public void updateUserById(UpdateUserRequest updateUserRequest, UserPrincipal currentUser) {

        String firstName = updateUserRequest.getFirstName();
        String lastName = updateUserRequest.getLastName();
        Long userId = currentUser.getId();

        log.info("Updating user info with user request firstname: \"{}\", lastname: \"{}\" by user with username: \"{}\"",
                firstName, lastName, currentUser.getUsername());

        if (userRepository.updateUserById(userId, firstName, lastName) == 0) {
            throw new BadRequestException("The update user operation could not be completed." +
                    " User not found or user has no rights.");
        }
    }

    public User getUserById(Long id) {
        log.info("Getting user with id: \"{}\"", id);
        return userRepository.findUserById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id + " or account is locked")
        );
    }
}
