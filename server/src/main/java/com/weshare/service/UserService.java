package com.weshare.service;

import com.weshare.dto.UserDTO;
import com.weshare.model.User;
import com.weshare.repository.UserRepository;
import com.weshare.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findCurrentUser() {
        return SecurityUtil.getCurrentUser();
    }

    public List<UserDTO> findUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getGroup().getId(),
                user.getRole());
    }

    public List<User> findUsersByNameIn(List<String> usernames) {
        return userRepository.findUsersByNameIn(usernames);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findUserById(id);
    }

}
