package com.example.accountservice.DTOs;

import com.example.accountservice.entity.Enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    //    private Integer id;
    private Integer accNumber;
    private double balance;
    private AccountType accountType;
    private Integer userId;
}
