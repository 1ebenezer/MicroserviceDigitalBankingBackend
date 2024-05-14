package com.example.authenticatorservice.repository;

import com.example.authenticatorservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String Email);

    User findByFirstName(String firstName);

//    Optional<User>existsByEmail(String Email);

}
