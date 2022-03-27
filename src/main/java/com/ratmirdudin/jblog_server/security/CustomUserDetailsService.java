package com.ratmirdudin.jblog_server.security;

import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        log.info("Loading user with username or email: \"{}\"", usernameOrEmail);
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "username or email", usernameOrEmail + " or account is locked")
        );

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long userId) {
        log.info("Loading user with id: \"{}\"", userId);
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId + " or account is locked")
        );

        return UserPrincipal.create(user);
    }
}