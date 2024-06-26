package com.example.accountservice.controller;

import com.example.accountservice.DTOs.*;
import com.example.accountservice.Validator.JwtValidator;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Transaction;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.services.AccountService;
import com.example.accountservice.services.Implementation.exceptions.BankAccountNotFoundException;
import com.example.accountservice.services.Implementation.exceptions.InsufficientFundsException;
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
@RequestMapping("/api/v1/account-service/accounts")
public class AccountController {
    @Autowired
    private final AccountService accountService;
    private final JwtValidator jwtValidator;
    private final AccountRepository accountRepository;

    @GetMapping
    public ResponseEntity<?> getaAllAccounts(
            @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(accountService.getAllAccounts(token));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestHeader("Authorization") String token,
            @RequestBody AccountRequest request,
            HttpServletRequest httpRequest
    ) {
        try {
            AccountInfo createdAccount = accountService.createAccount(token, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // update
    @PutMapping("/{accountNumber}")
    public ResponseEntity<?> updateAccount(
            @PathVariable Integer accountNumber,
            @RequestBody AccountUpdate accountUpdate) {

        try {
            Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
            System.out.println("here is the account controler");
            System.out.println(accountOptional);
            if (!accountOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }
            AccountInfo updatedAccount = accountService.updateAccount(accountNumber, accountUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
        } catch (Exception ex) {
            System.out.println(ex);
            throw new RuntimeException("======> The error is " + ex.getMessage());
        }
    }

    @GetMapping("/{accountNumber}")                                           //getbyAccNumber
    public ResponseEntity<?> getByAccountNumber(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer accountNumber) {
        try {
            Optional<Account> accountby = accountService.getByAccNumber(token, accountNumber);
            return ResponseEntity.status(HttpStatus.OK).body(accountby);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/dets/{accountNumber}")                                           //getbyAccNumber
    public ResponseEntity<Object> getAccountDetails(
            @PathVariable Integer accountNumber) {
        try {
            Optional<Account> account = accountService.getAccountDetails(accountNumber);
            if (account != null) {
                System.out.println(account);
                return ResponseEntity.status(HttpStatus.OK).body(account);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account details not found for account number: " + accountNumber);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve account details: " + e.getMessage());
        }
    }

    @GetMapping("/{accountNumber}/balance")                                           //VIEWBALANCE
    public ResponseEntity<?> viewBalance(
            @RequestHeader("Authorization") String token
            , @PathVariable Integer accountNumber) {

        try {
            double balance = accountService.viewBalance(accountNumber, token);
            return ResponseEntity.status(HttpStatus.OK).body(balance);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestHeader("Authorization") String token,
            @RequestBody WithdrawRequest request) {

        try {
            AccountInfo accountInfo = accountService.withdraw(token, request);
            return ResponseEntity.status(HttpStatus.OK).body(accountInfo);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestHeader("Authorization") String token,
                                     @RequestBody DepositRequest request) {
        try {
            AccountInfo deposit = accountService.deposit(token, request);
            System.out.println("Endpoint");
            return ResponseEntity.status(HttpStatus.OK).body(deposit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<?> transactionHistory(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer accountNumber) {
        try {
            List<Transaction> transactions = accountService.getTransactionHistory(token, accountNumber);
            return ResponseEntity.status(HttpStatus.OK).body(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
