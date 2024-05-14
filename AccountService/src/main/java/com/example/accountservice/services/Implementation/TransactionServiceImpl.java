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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    @Override
    public TransactionRequest withdrawRecord(Integer accNumber, Double amount) {
        return processTransaction(accNumber, amount, Type.WITHDRAW);
    }

    @Override
    public TransactionRequest depositRecord(Integer accNumber, Double amount) {
        return processTransaction(accNumber, amount, Type.DEPOSIT);
    }

    private TransactionRequest processTransaction(Integer accNumber, Double amount, Type type) {
        Optional<Account> optionalAccount = accountRepo.findByAccNumber(accNumber);
        Account account = optionalAccount.orElseThrow(() -> new RuntimeException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setTime(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setAccount(account);

        account.getTransactions().add(transaction);
        transactionRepo.save(transaction);

        return mapToTransaction(transaction);
    }

    private TransactionRequest mapToTransaction(Transaction transaction) {
        TransactionRequest request = new TransactionRequest();
        request.setId(transaction.getId());
        request.setTime(transaction.getTime());
        request.setType(transaction.getType());
        request.setAmount(transaction.getAmount());
        request.setAccNumber(transaction.getAccount().getAccNumber());

        return request;
    }
}
