package com.example.accountservice.DTOs;

import com.example.accountservice.entity.Enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Integer id;
    private Integer accountNumber;
    private double balance;
    private AccountType accountType;
    private Integer userId;
}
