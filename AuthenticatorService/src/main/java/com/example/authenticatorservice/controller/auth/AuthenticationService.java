package com.example.authenticatorservice.controller.auth;

import com.example.authenticatorservice.entity.Role;
import com.example.authenticatorservice.entity.User;
import com.example.authenticatorservice.repository.UserRepository;
import com.example.authenticatorservice.service.JwtService;
import com.example.authenticatorservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    public static class WrongPasswordException extends RuntimeException {
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    public static class AuthenticationFail extends RuntimeException {
        public AuthenticationFail(String message) {
            super(message);
        }
    }

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
            if (request.getEmail() == null || request.getPassword() == null) {
                throw new IllegalArgumentException("Email or password cannot be null");
            }

            if (request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Email or password cannot be empty");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).message("Successfully Authenticated").build();
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                throw new WrongPasswordException("Password rejected: ");
            } else {
                throw new AuthenticationFail("Authentication failed: ") {
                };
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid input: ");
        }
    }

}


