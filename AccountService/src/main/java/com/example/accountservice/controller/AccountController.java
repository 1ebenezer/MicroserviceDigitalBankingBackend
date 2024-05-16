package com.example.accountservice.controller;

import com.example.accountservice.DTOs.AccountInfo;
import com.example.accountservice.DTOs.AccountRequest;
import com.example.accountservice.DTOs.DepositRequest;
import com.example.accountservice.DTOs.WithdrawRequest;
import com.example.accountservice.Validator.JwtValidator;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Transaction;
import com.example.accountservice.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/Acount")
public class AccountController {
    @Autowired
    private final AccountService accountService;
    private final JwtValidator jwtValidator;

    @GetMapping("/read")
    public ResponseEntity<List<Account>> getaAllAccounts() {
        return ResponseEntity.ok(accountService.getaAllAccounts());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(
            @RequestHeader("Authorization") String token,
            @RequestBody AccountRequest request,
            HttpServletRequest httpRequest
    ) {
        try {
            AccountRequest createdAccount = accountService.createAccount(token, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/account/{accNumber}")                                           //getbyAccNumber
    public ResponseEntity<?> getById(@PathVariable Integer accNumber) {
        Optional<Account> accountby = accountService.getByAccNumber(accNumber);
        return ResponseEntity.status(HttpStatus.OK).body(accountby);
    }

    @GetMapping("/viewbalance/{accNumber}")                                           //VIEWBALANCE
    public ResponseEntity<Double> viewBalance(@PathVariable Integer accNumber) {
        double balance = accountService.viewBalance(accNumber);
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        AccountInfo accountInfo = accountService.withdraw(request);
        return ResponseEntity.status(HttpStatus.OK).body(accountInfo);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        AccountInfo deposit = accountService.deposit(request);
        return ResponseEntity.status(HttpStatus.OK).body(deposit);
    }

    @GetMapping("/{accNumber}/transactions")
    public ResponseEntity<List<Transaction>> transactionHistory(@PathVariable Integer accNumber) {
        List<Transaction> transactions = accountService.getTransactionHistory(accNumber);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

}
