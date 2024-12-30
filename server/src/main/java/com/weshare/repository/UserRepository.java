package com.weshare.repository;

import com.weshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByName(String name);
    Optional<User> findUserById(Integer id);

    List<User> findUsersByNameIn(List<String> names);

}
