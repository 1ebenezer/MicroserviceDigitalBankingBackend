package com.example.authenticatorservice.controller.user;

import com.example.authenticatorservice.entity.User;
import com.example.authenticatorservice.entity.dtos.ChangePasswordRequest;
import com.example.authenticatorservice.repository.UserRepository;
import com.example.authenticatorservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication-service/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userservice;
    private final UserRepository userRepository;

//    @PutMapping("/user/update")
//    public ResponseEntity<?> updateUser(@RequestBody User user) {
//        return ResponseEntity.ok(userservice.updateUser(user));
//    }

    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            if (!userservice.verifyOldPassword(changePasswordRequest.getEmail(), changePasswordRequest.getOldPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            userservice.changePassword(changePasswordRequest.getEmail(), changePasswordRequest.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);

    }
}
