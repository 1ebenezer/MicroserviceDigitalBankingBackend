package com.example.accountservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositRequest {
    private Integer accountNumber;
    private double amount;

    public void validate() {
        if (amount < 0) {
            throw new RuntimeException("Amount cannot be negative");
        }
    }
}
