package com.example.authenticatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticatorServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(AuthenticatorServiceApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
