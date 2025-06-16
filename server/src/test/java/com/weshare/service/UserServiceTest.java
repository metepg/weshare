package com.weshare.service;

import com.weshare.dto.UserDTO;
import com.weshare.model.Group;
import com.weshare.model.User;
import com.weshare.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityService securityService;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Returns current user from security service")
    void returnsCurrentUser() {
        UserDTO expected = new UserDTO(1, "test", UUID.randomUUID(), "ROLE_USER");
        when(securityService.getCurrentUser()).thenReturn(expected);

        UserDTO result = userService.findCurrentUser();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Returns all users as DTOs")
    void returnsAllUsersAsDTOs() {
        UUID groupId = UUID.randomUUID();
        Group group = new Group();
        group.setId(groupId);

        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setRole("ROLE_USER");
        user.setGroup(group);

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.findUsers();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Test");
        assertThat(result.getFirst().groupId()).isEqualTo(groupId);
    }

    @Test
    @DisplayName("Returns users by usernames")
    void returnsUsersByNames() {
        User user = new User();
        user.setName("test");

        when(userRepository.findUsersByNameIn(List.of("test"))).thenReturn(List.of(user));

        List<User> result = userService.findUsersByNameIn(List.of("test"));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("test");
    }

    @Test
    @DisplayName("Returns user by ID")
    void returnsUserById() {
        User user = new User();
        user.setId(1);

        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Returns empty when user not found by ID")
    void returnsEmptyWhenUserNotFoundById() {
        when(userRepository.findUserById(99)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(99);

        assertThat(result).isEmpty();
    }
}
