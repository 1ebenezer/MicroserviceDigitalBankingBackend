package com.example.authenticatorservice.controller.user;

import com.example.authenticatorservice.controller.auth.UserResponse;
import com.example.authenticatorservice.entity.User;
import com.example.authenticatorservice.entity.dtos.RoleToUser;
import com.example.authenticatorservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authentication-service/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userservice;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")                               // read all users
    public ResponseEntity<List<UserResponse>> findAllUsers() {

        return ResponseEntity.ok(userservice.findAllUsers());
    }

    @PostMapping("/users/roles")
    @PreAuthorize("hasRole('ADMIN')")                               //add role to user
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUser roleToUser) {
        System.out.println("hello i am here on controller");
        userservice.addRoleToUser(roleToUser.getEmail(), roleToUser.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{email}")
    @PreAuthorize("hasRole('ADMIN')")                               //findByEmail
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        User user = userservice.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users")                                     //update
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userservice.updateUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{email}")                                 //delete
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        try {
            System.out.println("yellow");
            userservice.deleteUserByEmail(email);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

