package com.weshare.service;

import com.weshare.model.Person;
import com.weshare.repository.PersonRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository1, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository1;
        this.passwordEncoder = passwordEncoder;
    }

    public Person createPerson(String name, String password, String role) {
        Person person = new Person();
        person.setUsername(name);
        person.setPassword(passwordEncoder.encode(password));
        person.setRole(role);
        return personRepository.save(person);
    }

    public List<Person> findPersons() {
        return personRepository.findAll();
    }

    public Person findCurrentPerson() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "";
        Optional<Person> person = personRepository.findByUsername(username);
        return person.orElse(null);
    }

}
