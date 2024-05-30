package com.example.TransferService.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private Integer accountNumber;
    private double balance;
    private AccountType accountType;
    private Integer userId;

}
