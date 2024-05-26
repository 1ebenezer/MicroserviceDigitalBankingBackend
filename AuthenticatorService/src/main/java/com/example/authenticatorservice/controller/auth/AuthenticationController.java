package com.example.authenticatorservice.controller.auth;

import com.example.authenticatorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication-service/users")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response;
        try {
            service.register(request);
            response = AuthenticationResponse.builder()
                    .message("Successfully saved").build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = AuthenticationResponse.builder()
                    .message("Registration Failed")
                    .build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response;
        try {
            response = service.authenticate(request);
            if (response.getMessage() != null && !response.getMessage().isEmpty()) {
                // Authentication was successful
                return ResponseEntity.ok(response);
            }
        } catch (AuthenticationService.WrongPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body
                    (new AuthenticationResponse(null, "Password or Username is wrong"));
        } catch (AuthenticationService.AuthenticationFail e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body
                    (new AuthenticationResponse(null, "Authentication failed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
