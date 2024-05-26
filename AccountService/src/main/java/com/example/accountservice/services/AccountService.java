package com.example.accountservice.services;


import com.example.accountservice.DTOs.AccountInfo;
import com.example.accountservice.DTOs.AccountRequest;
import com.example.accountservice.DTOs.DepositRequest;
import com.example.accountservice.DTOs.WithdrawRequest;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Transaction;
import com.example.accountservice.services.Implementation.exceptions.BankAccountNotFoundException;
import com.example.accountservice.services.Implementation.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {


    List<Account> getAllAccounts(String authorizationHeader);

    AccountInfo createAccount(String authorizationHeader, AccountRequest request);

    Optional<Account> getByAccNumber(String authorizationHeader, Integer accountNumber);

    double viewBalance(Integer accountNumber, String authorizationHeader);

    AccountInfo withdraw(String authorizationHeader, WithdrawRequest request) throws BankAccountNotFoundException, InsufficientFundsException;

    AccountInfo deposit(String authorizationHeader, DepositRequest depositRequest);

    List<Transaction> getTransactionHistory(String authorizationHeader, Integer accNumber);

    AccountInfo updateAccount(Integer accNumber, AccountInfo request, String authorizationHeader);
}
