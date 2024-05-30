package com.example.TransferService.Service.TransferImpl;

import com.example.TransferService.Entity.Account;
import com.example.TransferService.Entity.DTO.*;
import com.example.TransferService.Entity.Transfer;
import com.example.TransferService.Entity.TransferLogs;
import com.example.TransferService.Repository.TransferLogsRepository;
import com.example.TransferService.Repository.TransferRepository;
import com.example.TransferService.Service.TransferImpl.Exceptions.InsufficientBalanceException;
import com.example.TransferService.Service.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import com.example.TransferService.Entity.Status;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferServiceImplementation implements TransferService {

    @Autowired
    private final WebClient.Builder webClientBuilder;
    private final TransferRepository transferRepository;
    private final TransferLogsRepository transferLogsRepository;

    @Override
    public TransferResponse createTransfer(String senderToken, TransferDTO transferDTO) {
        validateSenderToken(senderToken);
        Account senderAccount = validateUserAccountAssociation(senderToken, transferDTO.getSenderAccount());
        if (senderAccount.getBalance() < transferDTO.getAmount()) {
            throw new InsufficientBalanceException("Sender does not have sufficient balance");
        }

        senderAccount.setBalance(senderAccount.getBalance() - transferDTO.getAmount());
        updateAccountDetails(senderAccount);

        Account receiverAccount = getAccountDetails(transferDTO.getReceiverAccount());
        System.out.println("receiver account details" +receiverAccount);

        if (receiverAccount == null || receiverAccount.getAccountNumber() == null) {
            throw new RuntimeException("Receiver account details not found.");
        }
        receiverAccount.setBalance(receiverAccount.getBalance() + transferDTO.getAmount());
        updateAccountDetails(receiverAccount);

        Transfer transfer = Transfer.builder()
                .senderAccount(transferDTO.getSenderAccount())
                .receiverAccount(transferDTO.getReceiverAccount())
                .amount(transferDTO.getAmount())
                .timestamp(LocalDateTime.now())
                .build();

        Transfer savedTransfer = transferRepository.save(transfer);

//        List<TransferLogs> transferLogs = new ArrayList<>();

        TransferLogs transferLog = TransferLogs.builder().
                transfer(savedTransfer)
                .senderAccount(senderAccount.getAccountNumber())
                .receiverAccount(receiverAccount.getAccountNumber())
                .timestamp(LocalDateTime.now())
                .amount(transferDTO.getAmount())
                .status(Status.DONE)
                .build();

        transferLogsRepository.save(transferLog);

        return TransferResponse.builder()
                .message("Transfer successful")
                .status(Status.DONE)
                .amount(transferDTO.getAmount())
                .build();
    }

    @Override
    public TransferResponseLogs createTransferLogs(TransferLogsDTO transferLogsDTO, Integer transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer not found with ID: " + transferId));

        TransferLogs transferLogs = TransferLogs.builder()
                .status(transferLogsDTO.getStatus())
                .receiverAccount(transferLogsDTO.getReceiverAccount())
                .senderAccount(transferLogsDTO.getSenderAccount())
                .timestamp(LocalDateTime.now())
                .amount(transferLogsDTO.getAmount())
                .transfer(transfer)
                .build();
        System.out.println("TransferLogsDTO: " + transferLogsDTO);

        TransferLogs savedTransferLogs = transferLogsRepository.save(transferLogs);

        return TransferResponseLogs.builder()
                .message("Transfer successful")
                .status(Status.DONE)
                .amount(transferLogsDTO.getAmount())
                .build();
    }


    @Override
    public List<TransferLogs> getTransferLogsBySender(Integer senderAccount) {
        List<TransferLogs> transferLogs = transferLogsRepository.findBySenderAccount(senderAccount);

        if (transferLogs.isEmpty()) {
            throw new RuntimeException("No transfer logs found for sender account: " + senderAccount);
        }
        return transferLogs;
    }



    private TransferLogsDTO convertToDto(TransferLogsDTO transferLogs) {
        return TransferLogsDTO.builder()
                .Id(transferLogs.getId())
                .senderAccount(transferLogs.getSenderAccount())
                .receiverAccount(transferLogs.getReceiverAccount())
                .amount(transferLogs.getAmount())
                .timestamp(transferLogs.getTimestamp())
                .status(transferLogs.getStatus())
                .build();
    }

    private void validateSenderToken(String senderToken) {
        WebClient webClient = WebClient.create("http://localhost:8080/api/v1/validator");

        try {
            webClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("token", senderToken).build())
                    .header(HttpHeaders.AUTHORIZATION,  senderToken) // Include bearer token in authorization header
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new RuntimeException("Sender token is not valid.");
            }
            throw new RuntimeException("Failed to validate sender token: " + ex.getMessage(), ex);
        }
    }

    private Account validateUserAccountAssociation(String token, Integer accountNumber) {
        WebClient webClient = webClientBuilder.build();
        String url = "http://localhost:8082/api/v1/account-service/accounts/{accountNumber}";

        return webClient.get()
                .uri(url, accountNumber)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Wrong Sender account Number Check for mistakes")))
                .bodyToMono(Account.class)
                .block();
    }

    private Account getAccountDetails( Integer accountNumber) {

        WebClient webClient = webClientBuilder.build();
        String url = "http://localhost:8082/api/v1/account-service/accounts/dets/{accountNumber}";
        try {
            Mono<Account> accountMono = webClient.get()
                    .uri(url, accountNumber)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("can not find the account,check account number")))
                    .bodyToMono(Account.class);
            Account receiverAccount = accountMono.block();
            return receiverAccount;
        } catch (Exception e) {
            throw new RuntimeException("wrong account number");
        }
    }
    private void updateAccountDetails(Account account) {
        WebClient webClient = webClientBuilder.build();
        String url="http://localhost:8082/api/v1/account-service/accounts/{accountNumber}";
        AccountUpdate accountUpdate = new AccountUpdate();
        accountUpdate.setBalance(account.getBalance());

        try {
            webClient.put()
                    .uri(url, account.getAccountNumber())
                    .bodyValue(accountUpdate)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            clientResponse -> Mono.error(new RuntimeException("Failed to update account details")))
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error updating account details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
