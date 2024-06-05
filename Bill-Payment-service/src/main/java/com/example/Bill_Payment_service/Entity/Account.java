package com.example.Bill_Payment_service.Entity;

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
    private Integer userId;
    
}
