package com.example.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(AccountServiceApplication.class, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
