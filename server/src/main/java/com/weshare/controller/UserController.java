package com.weshare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/users/current")
    public ResponseEntity<String> findUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth != null ? auth.getName() : "";

        return new ResponseEntity<>(username, HttpStatus.OK);
    }

}
