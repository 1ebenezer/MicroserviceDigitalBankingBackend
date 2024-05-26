package com.example.accountservice.services.Implementation;

import com.example.accountservice.DTOs.TransactionRequest;
import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Enums.Type;
import com.example.accountservice.entity.Transaction;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.TransactionRepository;
import com.example.accountservice.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    @Override
    public TransactionRequest withdrawRecord(Integer accountNumber, Double amount, Integer adminId, Double balanceAfterTransaction) {
        return processTransaction(accountNumber, amount, Type.WITHDRAW, adminId, balanceAfterTransaction);
    }

    @Override
    public TransactionRequest depositRecord(Integer accountNumber, Double amount, Integer adminId, Double balanceAfterTransaction) {
        return processTransaction(accountNumber, amount, Type.DEPOSIT, adminId, balanceAfterTransaction);
    }

    private TransactionRequest processTransaction(Integer accountNumber, Double amount, Type type, Integer adminId, Double balanceAfterTransaction) {
        Optional<Account> optionalAccount = accountRepo.findByAccountNumber(accountNumber);
        Account account = optionalAccount.orElseThrow(() -> new RuntimeException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setTime(formatLocalDateTime(LocalDateTime.now()));
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setAccount(account);
        transaction.setAdminId(adminId);
        transaction.setBalanceAfterTransaction(balanceAfterTransaction);
//        double newBalance = type == Type.DEPOSIT ? account.getBalance() + amount : account.getBalance() - amount;
//        transaction.setBalanceAfterTransaction(newBalance);

        account.getTransactions().add(transaction);
        transactionRepo.save(transaction);

        return mapToTransaction(transaction);
    }

    private LocalDateTime formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }

    private TransactionRequest mapToTransaction(Transaction transaction) {
        TransactionRequest request = new TransactionRequest();
        request.setId(transaction.getId());
        request.setTime(transaction.getTime());
        request.setType(transaction.getType());
        request.setAmount(transaction.getAmount());
        request.setAccountNumber(transaction.getAccount().getAccountNumber());
        request.setBalanceAfterTransaction(transaction.getBalanceAfterTransaction());
        request.setAdminId(transaction.getAdminId());
        return request;
    }
}
