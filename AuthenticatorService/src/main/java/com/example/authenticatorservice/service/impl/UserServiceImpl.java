package com.example.authenticatorservice.service.impl;

import com.example.authenticatorservice.controller.auth.UserResponse;
import com.example.authenticatorservice.entity.Role;
import com.example.authenticatorservice.entity.User;
import com.example.authenticatorservice.repository.UserRepository;
import com.example.authenticatorservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
        log.info("Welcome {} to the database", user.getFirstName());
        return userRepo.save(user);
    }


    @Override
    public User updateUser(User user) {
        log.info("your data has been well,edited {} to the database", user.getFirstName());

        User existingUser = userRepo.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
            return userRepo.save(existingUser);
        } else {
            throw new EntityNotFoundException("User not found with id: " + user.getId());
        }
    }

    @Override
    public User getUserByEmail(String email) {

        return userRepo.findByEmail(email).orElseThrow(null);
    }

    @Override
    public void addRoleToUser(String email, Role role) {
        log.info("adding role {} to email {}", role, email);

        User user = userRepo.findByEmail(email).orElseThrow(null);
        if (user != null) {
            user.setRole(role);
            userRepo.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
//        User user = userRepo.findByEmail(email).orElse(null);
//
//        if (user != null) {
//            userRepo.delete(user);
//        } else {
//            throw new RuntimeException("User with email " + email + " not found");
//        }
        try {
            Optional<User> user = userRepo.findByEmail(email);
            if (user.isPresent()) {
                log.info("Deleting user: " + user.get());
                userRepo.deleteByEmail(email);
            } else {
                log.warn("User with email " + email + " not found.");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting user with email " + email, e);
        }
    }

    @Override
    public void changePassword(String email, String newPassword) {
        System.out.println("i am here");
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    @Override
    public boolean verifyOldPassword(String email, String oldPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException
                        ("User not found " + email));
        String encodedPassword = user.getPassword();
        return passwordEncoder.matches(oldPassword, encodedPassword);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        log.info("All users");
        return userRepo.findAllUsers();
    }

    @Override
    public User getUser(String firstName) {
        log.info("looking for user {}", firstName);
        return userRepo.findByFirstName(firstName);
    }

    @Override
    public List<User> getUsers() {
        log.info("All users");
        return userRepo.findAll();
    }
}
