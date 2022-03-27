package com.ratmirdudin.jblog_server.services;

import com.ratmirdudin.jblog_server.exceptions.BadRequestException;
import com.ratmirdudin.jblog_server.exceptions.ResourceNotFoundException;
import com.ratmirdudin.jblog_server.models.entities.Role;
import com.ratmirdudin.jblog_server.models.entities.User;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import com.ratmirdudin.jblog_server.payloads.requests.UpdateUserRequest;
import com.ratmirdudin.jblog_server.repositories.UserRepository;
import com.ratmirdudin.jblog_server.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private User userRatmir;
    private User userDmitriy;

    @BeforeEach
    void setUp() {
        Role roleUser = new Role(1L, RoleEnum.ROLE_USER);
        Role roleAdmin = new Role(2L, RoleEnum.ROLE_ADMIN);
        userRatmir = new User(
                1L,
                "Ratmir",
                "Dudin",
                "ratmir",
                "ratmir@mail.ru",
                "password",
                true,
                Set.of(roleUser, roleAdmin)
        );
        userDmitriy = new User(
                2L,
                "Dmitriy",
                "Lobanov",
                "dmitriy",
                "dmitriy@mail.ru",
                "password",
                false,
                Set.of(roleUser)
        );
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        Optional<User> optionalUserRatmir = Optional.of(userRatmir);
        when(userRepository.findUserById(anyLong())).thenReturn(optionalUserRatmir);
        User userById = userService.getUserById(1L);

        assertNotNull(userById);
        assertEquals("ratmir", userById.getUsername());
    }

    @Test
    void whenGetUserById_thenThrowResourceNotFoundException() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L)
        );
    }


    @Test
    void whenGetAllUsers_thenReturnPageOfUsers() {
        List<User> userList = List.of(userRatmir, userDmitriy);
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("createdAt").descending());
        when(userRepository.findAllUsers("", pageable))
                .thenReturn(
                        new PageImpl<>(userList, pageable, userList.size())
                );

        Page<User> userPage = userService.getAllUsers("", 0, 2);

        assertEquals(2, userPage.getSize());
    }

    @Test
    void whenUpdateUserById_thenUpdate() {

        userRatmir.setId(1L);
        when(userRepository.updateUserById(userRatmir.getId(), "firstname", "lastname")).thenReturn(1);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("firstname", "lastname");
        UserPrincipal currentUser = UserPrincipal.create(userRatmir);

        userService.updateUserById(updateUserRequest, currentUser);

    }

    @Test
    void whenUpdateUserById_thenThrowBadRequestException() {
        userRatmir.setId(1L);
        when(userRepository.updateUserById(userRatmir.getId(), "firstname", "lastname")).thenReturn(0);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("firstname", "lastname");
        UserPrincipal currentUser = UserPrincipal.create(userRatmir);


        assertThrows(BadRequestException.class,
                () -> userService.updateUserById(updateUserRequest, currentUser)
        );
    }
}
