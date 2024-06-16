package com.weshare.controller;

import com.weshare.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("/current")
    public ResponseEntity<String> findPerson() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth != null ? auth.getName() : "";

        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@ERole.ROLE1, @ERole.ROLE2)")
    @GetMapping("")
    public List<String> findPersons() {
        return personService.findPersons();
    }

}
