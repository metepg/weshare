package com.weshare.service;

import com.weshare.dto.UserDTO;
import com.weshare.model.User;
import com.weshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findUserByName(username)
            .map(this::convertToDTO)
            .orElseThrow(() -> new IllegalStateException("User not found in the database"));
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getGroup().getId(),
            user.getRole());
    }
}
