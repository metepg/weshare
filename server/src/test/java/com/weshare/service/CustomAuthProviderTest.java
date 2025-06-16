package com.weshare.service;

import com.weshare.model.User;
import com.weshare.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthProviderTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    CustomAuthProvider authProvider;

    @Test
    @DisplayName("Authenticates successfully with correct credentials")
    void authenticatesSuccessfully() {
        User user = new User();
        user.setName("test");
        user.setPassword("encoded");
        user.setRole("USER");

        Authentication auth = new UsernamePasswordAuthenticationToken("test", "raw");

        when(userRepository.findUserByName("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);

        Authentication result = authProvider.authenticate(auth);

        assertEquals("test", result.getName());
        assertEquals("raw", result.getCredentials());
        assertTrue(result.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Throws if user not found")
    void throwsWhenUserNotFound() {
        Authentication auth = new UsernamePasswordAuthenticationToken("missing", "pass");

        when(userRepository.findUserByName("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authProvider.authenticate(auth))
            .isInstanceOf(BadCredentialsException.class)
            .hasMessage("Väärä nimi tai salasana");
    }

    @Test
    @DisplayName("Throws if password does not match")
    void throwsWhenPasswordIncorrect() {
        User user = new User();
        user.setName("test");
        user.setPassword("encoded");

        Authentication auth = new UsernamePasswordAuthenticationToken("test", "wrong");

        when(userRepository.findUserByName("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> authProvider.authenticate(auth))
            .isInstanceOf(BadCredentialsException.class)
            .hasMessage("Väärä nimi tai salasana");
    }
}
