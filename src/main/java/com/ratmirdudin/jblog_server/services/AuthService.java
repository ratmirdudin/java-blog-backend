package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.cachings.RoleCachingService;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import com.ratmirdudin.jblog_server.payloads.requests.LoginRequest;
import com.ratmirdudin.jblog_server.payloads.requests.SignUpRequest;
import com.ratmirdudin.jblog_server.repositories.UserRepository;
import com.ratmirdudin.jblog_server.security.JwtTokenProvider;
import com.ratmirdudin.jblog_server.security.SecurityConstants;
import com.ratmirdudin.jblog_server.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleCachingService roleCachingService;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    public String authenticateUser(LoginRequest loginRequest) {
        log.info("Authenticating user with username: \"{}\"", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);
    }

    @Transactional
    public User createUser(SignUpRequest signUpRequest) {
        log.info("Creating user with username: \"{}\"", signUpRequest.getUsername());
        User user = new User();
        user.setFirstname(signUpRequest.getFirstname());
        user.setLastname(signUpRequest.getLastname());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setEncryptedPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        if (activeProfile.equals("prod")) {
            user.setVerified(false);
            user.setVerificationCode(Utils.generateSecureRandomString(30));
        } else if (activeProfile.equals("dev")) {
            user.setVerified(true);
            user.setVerificationCode(null);
        }

        user.setEnabled(true);
        user.setImmunity(false);

        user.setRoles(Set.of(roleCachingService.getRole(RoleEnum.ROLE_USER).orElseThrow(
                () -> new ResourceNotFoundException("Role", "role name", RoleEnum.ROLE_USER.name())
        )));

        userRepository.save(user);
        return user;
    }

    public void verifyUserAccount(String verificationCode) {
        log.info("Verifying user account with verification code: \"{}\"", verificationCode);
        if (userRepository.verifyUserAccount(verificationCode) == 0) {
            throw new ResourceNotFoundException("User", "verification code", verificationCode);
        }

    }

}
