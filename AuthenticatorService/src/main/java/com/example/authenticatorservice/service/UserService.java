package com.example.authenticatorservice.service;

import com.example.authenticatorservice.entity.Role;
import com.example.authenticatorservice.entity.User;

import java.util.List;


public interface UserService {
    User saveUser(User user);

    User updateUser(User user);

    User getUserByEmail(String email);

    void addRoleToUser(String email, Role role);

    void deleteUserByEmail(String email);

    void changePassword(String email, String newPassword);

    boolean verifyOldPassword(String email, String oldPassword);

    User getUser(String firstName);

    List<User> getUsers();

}
