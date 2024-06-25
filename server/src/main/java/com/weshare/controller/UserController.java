package com.weshare.controller;

import com.weshare.model.User;
import com.weshare.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/current")
    public User findCurrentUser() {
        return userService.findCurrentUser();
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("")
    public List<User> findUsers() {
        return userService.findUsers();
    }

}