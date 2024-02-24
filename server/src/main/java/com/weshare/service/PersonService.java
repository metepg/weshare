package com.weshare.service;

import com.weshare.model.Person;
import com.weshare.repository.IPersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final IPersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(IPersonRepository personRepository1, PasswordEncoder passwordEncoder) {
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
}
