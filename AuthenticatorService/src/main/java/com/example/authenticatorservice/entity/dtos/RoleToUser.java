package com.example.authenticatorservice.entity.dtos;

import com.example.authenticatorservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleToUser {
    String email;
    Role role;
}
