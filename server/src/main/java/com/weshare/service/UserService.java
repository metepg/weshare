package com.weshare.service;

import com.weshare.dto.UserDTO;
import com.weshare.model.User;
import com.weshare.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BillService billService;

    public UserService(UserRepository userRepository, BillService billService) {
        this.userRepository = userRepository;
        this.billService = billService;
    }

    public UserDTO findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth != null ? auth.getName() : "";
        Optional<User> user = userRepository.findUserByName(name);
        return user.map(this::convertToDTO).orElse(null);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public double findUserDebtByUserId(Integer id) {
        return billService.findUserDebtByUserId(id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getGroup().getId(),
                user.getRole());
    }

}
