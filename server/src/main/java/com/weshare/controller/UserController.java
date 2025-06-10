package com.weshare.controller;

import com.weshare.dto.UserDTO;
import com.weshare.service.BillService;
import com.weshare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BillService billService;

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public UserDTO findCurrentUser() {
        return userService.findCurrentUser();
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public List<UserDTO> findUsers() {
        return userService.findUsers();
    }

    @GetMapping("/{userId}/debt")
    @PreAuthorize("hasAnyRole(@eRole.role1, @eRole.role2)")
    public double findUserDebtByUserId(@PathVariable Integer userId) {
        return billService.findUserDebtByUserId(userId);
    }

}
