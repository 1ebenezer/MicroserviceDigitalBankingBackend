package com.example.accountservice.entity;

import com.example.accountservice.entity.Enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer accountNumber;
    private double balance = 0.0;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private Integer userId;
    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Account() {

        this.accountNumber = generatedAccountNumber();
    }

    private Integer generatedAccountNumber() {

        return generatedRandomNumber();
    }

    private Integer generatedRandomNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
