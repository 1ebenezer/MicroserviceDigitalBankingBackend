package com.example.accountservice.services;


import com.example.accountservice.DTOs.AccountInfo;
import com.example.accountservice.DTOs.AccountRequest;
import com.example.accountservice.DTOs.DepositRequest;
import com.example.accountservice.DTOs.WithdrawRequest;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {
    List<Account> getaAllAccounts();

    AccountRequest createAccount(String authorizationHeader, AccountRequest request);

//    AccountRequest createAccount(String authorizationHeader, AccountType accType);

    Optional<Account> getByAccNumber(Integer accNumber);

    double viewBalance(Integer accNumber);

    AccountInfo withdraw(WithdrawRequest request);

    AccountInfo deposit(DepositRequest depositRequest);

    List<Transaction> getTransactionHistory(Integer accNumber);


}
