package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.cachings.RoleCachingService;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.Role;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import com.ratmirdudin.jblog_server.payloads.requests.SignUpRequest;
import com.ratmirdudin.jblog_server.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleCachingService roleCachingService;
    @InjectMocks
    private AuthService authService;

    @Test
    void whenCreateUser_thenReturnCreatedUser() {
        when(roleCachingService.getRole(RoleEnum.ROLE_USER))
                .thenReturn(Optional.of(new Role(1L, RoleEnum.ROLE_USER)));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User createdUser = authService.createUser(new SignUpRequest(
                "ratmir",
                "ratmir@mail.ru",
                "password",
                "Firstname",
                "Lastname"
        ));

        assertEquals("ratmir", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getEncryptedPassword());
        assertNotNull(createdUser.getVerificationCode());
        assertFalse(createdUser.isVerified());
        assertFalse(createdUser.isImmunity());
        assertTrue(createdUser.isEnabled());

    }

    @Test
    void whenCreateUser_thenReturnResourceNotFoundException() {
        when(roleCachingService.getRole(RoleEnum.ROLE_USER))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertThrows(ResourceNotFoundException.class,
                () -> authService.createUser(new SignUpRequest(
                        "ratmir",
                        "ratmir@mail.ru",
                        "password",
                        "Firstname",
                        "Lastname"
                ))
        );

    }

    @Test
    void whenVerifyUserAccount_thenVerifyUser() {
        when(userRepository.verifyUserAccount(anyString())).thenReturn(1);

        authService.verifyUserAccount("f1j5df6JSD8FDMgVKSV");
    }

    @Test
    void whenVerifyUserAccount_thenThrowResourceNotFoundException() {
        when(userRepository.verifyUserAccount(anyString())).thenReturn(0);
        assertThrows(ResourceNotFoundException.class,
                () -> authService.verifyUserAccount("f1j5df6JSD8FDMgVKSV")
        );
    }
}