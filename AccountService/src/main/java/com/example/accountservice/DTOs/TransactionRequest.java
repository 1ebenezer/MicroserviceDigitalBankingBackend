package com.example.accountservice.DTOs;

import com.example.accountservice.entity.Enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private Integer id;
    private LocalDateTime time;
    private Type type;
    private Double amount;
    private Integer accNumber;
}
