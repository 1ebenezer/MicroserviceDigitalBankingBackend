package com.example.authenticatorservice.repository;

import com.example.authenticatorservice.controller.auth.UserResponse;
import com.example.authenticatorservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String Email);

    @Query("SELECT new com.example.authenticatorservice.controller.auth.UserResponse(u.firstName, u.lastName, u.email) FROM User u")
    List<UserResponse> findAllUsers();

    User findByFirstName(String firstName);

    Optional<User> findById(Integer id);


    void deleteByEmail(@Param("email") String email);   //

//    Optional<User>existsByEmail(String Email);

}
