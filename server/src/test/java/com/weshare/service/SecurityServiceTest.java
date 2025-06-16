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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    SecurityService securityService;

    @Test
    @DisplayName("Returns current user when authenticated")
    void returnsCurrentUser() {
        String username = "username";
        String password = "password";
        String role = "ROLE_1";
        UUID groupId = UUID.randomUUID();
        Group group = new Group();
        group.setId(groupId);

        User user = new User();
        user.setId(1);
        user.setName(username);
        user.setRole(role);
        user.setGroup(group);

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(username, password, List.of())
        );

        when(userRepository.findUserByName(username)).thenReturn(Optional.of(user));

        UserDTO result = securityService.getCurrentUser();

        assertThat(result.id()).isEqualTo(1);
        assertThat(result.name()).isEqualTo(username);
        assertThat(result.groupId()).isEqualTo(groupId);
        assertThat(result.role()).isEqualTo(role);
    }

    @Test
    @DisplayName("Throws when no authentication set")
    void throwsWhenNotAuthenticated() {
        SecurityContextHolder.clearContext();

        assertThatThrownBy(() -> securityService.getCurrentUser())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("User not authenticated");
    }

    @Test
    @DisplayName("Throws when principal is anonymousUser")
    void throwsWhenPrincipalIsAnonymousUser() {
        Authentication auth = new UsernamePasswordAuthenticationToken("anonymousUser", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThatThrownBy(() -> securityService.getCurrentUser())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("User not authenticated");
    }

    @Test
    @DisplayName("Throws when user not found in DB")
    void throwsWhenUserNotFound() {
        String username = "username";
        String password = "password";
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(username, password, List.of())
        );

        when(userRepository.findUserByName(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> securityService.getCurrentUser())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("User not found in the database");
    }

    @Test
    @DisplayName("Throws when authentication exists but isAuthenticated() is false")
    void throwsWhenAuthenticationNotAuthenticated() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThatThrownBy(() -> securityService.getCurrentUser())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("User not authenticated");
    }
}
