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
import com.example.accountservice.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private HttpServletRequest request;

    private SecurityContext securityContext;

    @Override
    public List<Account> getaAllAccounts() {
        return accRepo.findAll();
    }

    @Override
    public AccountRequest createAccount(AccountRequest request) {
//        String authorizationHeader = request.getAuthorizationHeader();
//        System.out.println("authorizationHeader");
//        System.out.println(authorizationHeader);
//        String token = extractTokenFromHeader(authorizationHeader);
//        if (token == null) {
//            throw new RuntimeException("Missing authorization header");
//        }
//        if (!jwtValidator.isTokenValid(token)) {
//            throw new RuntimeException("Invalid authorization token");
//        }
//        Integer userId = Integer.parseInt(jwtValidator.extractUserId(token));
//        Account account = new Account();
//        account.setUserId(userId);
//        Account savedAccount = accRepo.save(account);
//        return convertToDTO(savedAccount);

        return null;
    }

    private String extractTokenFromHeader(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }

    private AccountRequest convertToDTO(Account account) {
        AccountRequest request = new AccountRequest();
        BeanUtils.copyProperties(account, request);
        return request;
    }

    @Override
    public Optional<Account> getByAccNumber(Integer accNumber) {
        return accRepo.findByAccNumber(accNumber);
    }

    @Override
    public double viewBalance(Integer accNumber) {
        Optional<Account> Acc = accRepo.findByAccNumber(accNumber);
        if (Acc.isPresent()) {
            return Acc.get().getBalance();
        }
        throw new RuntimeException("Account not found");
    }

    @Override
    public AccountInfo withdraw(WithdrawRequest request) {
        Optional<Account> acc = accRepo.findByAccNumber(request.getAccNumber());
        if (acc.isPresent()) {
            Account account = acc.get();
            double currentBalance = account.getBalance();
            double amount = request.getAmount();
            if (currentBalance >= amount) {
                account.setBalance(currentBalance - amount);
                Account updatedAccount = accRepo.save(account);
                transactionService.withdrawRecord(acc.get().getAccNumber(), amount);
                return mapToAccountInfo(updatedAccount);
            } else {
                throw new RuntimeException("Insufficient funds");
            }
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    private AccountInfo mapToAccountInfo(Account account) {
        return AccountInfo.builder()
                .balance(account.getBalance())
                .accType(account.getAccType())
                .build();
    }

    @Override
    public AccountInfo deposit(DepositRequest depositRequest) {
        Optional<Account> acc = accRepo.findByAccNumber(depositRequest.getAccNumber());
        if (acc.isPresent()) {
            Account account = acc.get();
            double currentBalance = account.getBalance();
            double amount = depositRequest.getAmount();
            account.setBalance(currentBalance + amount);
            Account updatedAccount = accRepo.save(account);
            transactionService.depositRecord(acc.get().getAccNumber(), amount);
            return mapToAccountInfo(updatedAccount);
        } else {
            throw new RuntimeException("account number not found");
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(Integer accNumber) {
        Optional<Account> account = accRepo.findByAccNumber(accNumber);
        if (account.isPresent()) {
            Account acc = account.get();
            return acc.getTransactions();
        } else {
            throw new RuntimeException("account is not found");
        }
    }
}
