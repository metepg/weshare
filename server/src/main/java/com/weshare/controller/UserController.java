package com.weshare.controller;

import com.weshare.dto.UserDTO;
import com.weshare.model.User;
import com.weshare.service.DebtService;
import com.weshare.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final DebtService debtService;

    public UserController(UserService userService, DebtService debtService) {
        this.userService = userService;
        this.debtService = debtService;
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/current")
    public UserDTO findCurrentUser() {
        return userService.findCurrentUser();
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("")
    public List<User> findUsers() {
        return userService.findUsers();
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/{userId}/debt")
    public double getTotalDebtAmountByUserId(@PathVariable Integer userId) {
        return debtService.calculateTotalDebt(userId);
    }

}
