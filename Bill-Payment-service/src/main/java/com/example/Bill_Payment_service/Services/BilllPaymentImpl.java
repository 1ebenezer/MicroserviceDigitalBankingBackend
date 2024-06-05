package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.AccountUpdate;
import com.example.Bill_Payment_service.DTO.BillPaymentDTO;
import com.example.Bill_Payment_service.Entity.Account;
import com.example.Bill_Payment_service.Repository.BillPaymentRepository;
import com.example.Bill_Payment_service.Services.Exception.InsufficientBalanceException;
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

@Service
@Transactional
@RequiredArgsConstructor
public class BilllPaymentImpl implements BillPaymentService {

    @Autowired
    private final WebClient.Builder webclientBuilder;
    private final BillPaymentRepository billPaymentRepository;

    @Override
    public BillPaymentDTO makePayment(String token, BillPaymentDTO billPaymentDTO) {
        validateToken(token);
        Account senderAccount = validateUserAccountAssociation(token, billPaymentDTO.getAccountNumber());
        if (senderAccount.getBalance() < billPaymentDTO.getAmount()) {
            throw new InsufficientBalanceException("you don't have sufficient balance");
        }
        senderAccount.setBalance(senderAccount.getBalance() - billPaymentDTO.getAmount());
        updateAccountDetails(senderAccount);


        return null;
    }

    private void validateToken(String token) {

        WebClient webClient = WebClient.create("http://localhost:8080/api/v1/validator");
        try {
            webClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("token", token).build())
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new RuntimeException("token is not valid or expired");
            }
            throw new RuntimeException("failed to validate token" + ex.getMessage(), ex);
        }
    }

    private Account validateUserAccountAssociation(String token, Integer accountNumber) {
        WebClient webClient = webclientBuilder.build();
        String url = "http://localhost:8082/api/v1/account-service/accounts/{accountNumber}";

        return webClient.get()
                .uri(url, accountNumber)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Wrong Sender account Number. Check for mistakes.")))
                .bodyToMono(Account.class)
                .block();
    }

    private void updateAccountDetails(Account account) {

        WebClient webClient = webclientBuilder.build();
        String url = "http://localhost:8082/api/v1/account-service/accounts/{accountNumber}";
        AccountUpdate accountUpdate = new AccountUpdate();
        try {
            webClient.put()
                    .uri(url, account.getBalance())
                    .bodyValue(accountUpdate)
                    .retrieve().onStatus(HttpStatusCode::isError, clientResponse -> Mono.error
                            (new RuntimeException("Failed to update account details")))
                    .bodyToMono(Void.class).block();
        } catch (Exception e) {
            System.out.println("Error updating account details: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
