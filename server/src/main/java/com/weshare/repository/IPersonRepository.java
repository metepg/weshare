package com.weshare.repository;

import com.weshare.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);

    Boolean existsByUsername(String username);

}
