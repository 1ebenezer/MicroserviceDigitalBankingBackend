package com.example.accountservice.services;

import com.example.accountservice.DTOs.TransactionRequest;
import org.springframework.stereotype.Service;

@Service


public interface TransactionService {

    TransactionRequest withdrawRecord(Integer accNumber, Double amount);

    TransactionRequest depositRecord(Integer accNumber, Double amount);

}
