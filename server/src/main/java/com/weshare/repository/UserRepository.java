package com.weshare.repository;

import com.weshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByName(String name);
    Optional<User> findUserById(Long id);

    Optional<List<User>> findUsersByNameIn(List<String> names);

}
