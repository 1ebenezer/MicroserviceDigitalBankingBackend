package com.example.authenticatorservice.controller.auth;

import com.example.authenticatorservice.entity.Role;
import com.example.authenticatorservice.entity.User;
import com.example.authenticatorservice.repository.UserRepository;
import com.example.authenticatorservice.service.JwtService;
import com.example.authenticatorservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //register request
    public AuthenticationResponse register(RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Email is already registered");
            }
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(encodedPassword)
                    .role(Role.USER)
                    .build();

            userService.saveUser(user);

            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occured during user Registration");
        }
    }

    //authenticate request
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        } catch (AuthenticationException e) {

            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
//         catch (BadCredentialsException e) {
//
//            e.printStackTrace();
//            throw new RuntimeException("Authentication failed: " + e.getMessage());
//
//        }
    }
}

