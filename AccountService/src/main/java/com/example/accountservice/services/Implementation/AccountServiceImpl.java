package com.example.accountservice.services.Implementation;

import com.example.accountservice.DTOs.AccountInfo;
import com.example.accountservice.DTOs.AccountRequest;
import com.example.accountservice.DTOs.DepositRequest;
import com.example.accountservice.DTOs.WithdrawRequest;
import com.example.accountservice.Validator.JwtValidator;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Transaction;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.services.AccountService;
import com.example.accountservice.services.Implementation.exceptions.BankAccountNotFoundException;
import com.example.accountservice.services.Implementation.exceptions.InsufficientFundsException;
import com.example.accountservice.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtValidator jwtValidator;
    private SecurityContext securityContext;
    private String secretKey = "FDA6A5B0436E06FB69F8A89FA6E2E89798E164935BFB4AD099748C2480E4AE2A";

    @Override
    public List<Account> getAllAccounts(String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader, List.of("ADMIN"));
        return accRepo.findAll();
    }

    @Override
    public AccountInfo createAccount(String authorizationHeader, AccountRequest request) {
        String token = validateAuthorizationHeader(authorizationHeader, List.of("USER"));
        Integer userId = Integer.parseInt(jwtValidator.extractUserId(token));
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountType(request.getAccountType());
        Account savedAccount = accRepo.save(account);
        return mapToAccountInfo(savedAccount);
    }

    @Override
    public Optional<Account> getByAccNumber(String authorizationHeader, Integer accountNumber) {
        String token = validateAuthorizationHeader(authorizationHeader, List.of("USER"));
        return Optional.of(validateAccountByAccNumberAndUserId(accountNumber, token));

    }

    @Override
    public double viewBalance(Integer accountNumber, String authorizationHeader) {
        String token = validateAuthorizationHeader(authorizationHeader, List.of("USER", "ADMIN"));
        Account account = validateAccountByAccNumberAndUserId(accountNumber, token);
        return account.getBalance();
    }

    @Override
    public AccountInfo withdraw(String authorizationHeader, WithdrawRequest request) throws BankAccountNotFoundException, InsufficientFundsException {
        String token = validateAuthorizationHeader(authorizationHeader, List.of("ADMIN"));
        Account account = accRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));

        double currentBalance = account.getBalance();
        double amount = request.getAmount();

        if (currentBalance >= amount && amount >= 0) {
            account.setBalance(currentBalance - amount);
            Account updatedAccount = accRepo.save(account);
            Integer adminUserId = Integer.parseInt(jwtValidator.extractUserId(token));
            transactionService.withdrawRecord(request.getAccountNumber(), amount, adminUserId, updatedAccount.getBalance());
            return mapToAccountInfo(updatedAccount);
        } else {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    @Override
    public AccountInfo deposit(String authorizationHeader, DepositRequest depositRequest) {
        depositRequest.validate();
        String token = validateAuthorizationHeader(authorizationHeader, List.of("ADMIN"));
        Account account = accRepo.findByAccountNumber(depositRequest.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double amount = depositRequest.getAmount();
        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accRepo.save(account);
        Integer adminUserId = Integer.parseInt(jwtValidator.extractUserId(token));
        transactionService.depositRecord(depositRequest.getAccountNumber(), amount, adminUserId, updatedAccount.getBalance());
        return mapToAccountInfo(updatedAccount);
    }

    @Override
    public List<Transaction> getTransactionHistory(String authorizationHeader, Integer accNumber) {
        validateAuthorizationHeader(authorizationHeader, List.of("USER", "ADMIN"));
        return accRepo.findByAccountNumber(accNumber)
                .map(Account::getTransactions)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public AccountInfo updateAccount(Integer accNumber, AccountInfo request, String authorizationHeader) {
        Account account = accRepo.findByAccountNumber(accNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getBalance() != 0) {
            account.setBalance(request.getBalance());
        }
        if (request.getAccountType() != null) {
            account.setAccountType(request.getAccountType());
        }

        Account updatedAccount = accRepo.save(account);
        return convertToDTO(updatedAccount);
    }

    private String validateAuthorizationHeader(String authorizationHeader, List<String> allowedRoles) {
        String token = extractTokenFromHeader(authorizationHeader);
        if (token == null || !jwtValidator.isTokenValid(token)) {
            throw new RuntimeException("Missing or invalid token");
        }
        List<String> roles = jwtValidator.extractUserRoleFromToken(token);
        boolean hasAllowedRole = roles.stream().anyMatch(allowedRoles::contains);
        if (!hasAllowedRole) {
            throw new RuntimeException("NOT ALLOWED");
        }
        return token;
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }

    private AccountInfo mapToAccountInfo(Account account) {
        return AccountInfo.builder()
                .balance(account.getBalance())
                .accNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .userId(account.getUserId())
                .build();
    }

    private Account validateAccountByAccNumberAndUserId(Integer accountNumber, String token) {
        Integer userId = Integer.parseInt(jwtValidator.extractUserId(token));
        Optional<Account> accountOptional = accRepo.findByAccountNumber(accountNumber);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (account.getUserId().equals(userId)) {
                return account;
            } else {
                throw new RuntimeException("Wrong account number");
            }
        } else {
            throw new RuntimeException("Account not found");
        }
    }


    private AccountInfo convertToDTO(Account account) {
        AccountInfo request = new AccountInfo();
        BeanUtils.copyProperties(account, request);
        return request;
    }
}