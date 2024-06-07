package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.AccountUpdate;
import com.example.Bill_Payment_service.DTO.AirtimeDto;
import com.example.Bill_Payment_service.DTO.BillPaymentDTO;
import com.example.Bill_Payment_service.Entity.Account;
import com.example.Bill_Payment_service.Entity.BillPayment;
import com.example.Bill_Payment_service.Entity.PaymentLog;
import com.example.Bill_Payment_service.Entity.Status;
import com.example.Bill_Payment_service.Repository.BillPaymentRepository;
import com.example.Bill_Payment_service.Repository.PaymentRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BilllPaymentImpl implements BillPaymentService {

    @Autowired
    private final WebClient.Builder webclientBuilder;
    private final BillPaymentRepository billPaymentRepository;

    private final PaymentRepository paymentRepository;

    @Override
    public BillPaymentDTO makePayment(String token, BillPaymentDTO billPaymentDTO) {
        validateToken(token);
        Account senderAccount = validateUserAccountAssociation(token, billPaymentDTO.getAccountNumber());
        System.out.println("helloo======>" + senderAccount.getBalance());
        if (senderAccount.getBalance() < billPaymentDTO.getAmount()) {
            throw new InsufficientBalanceException("you don't have sufficient balance");
        }
        senderAccount.setBalance(senderAccount.getBalance() - billPaymentDTO.getAmount());
        updateAccountDetails(senderAccount);

        BillPayment billPayment = new BillPayment();
        billPayment.setAccountNumber(billPaymentDTO.getAccountNumber());
        billPayment.setAmount(billPaymentDTO.getAmount());
        billPayment.setBiller(billPaymentDTO.getBiller());


        billPaymentRepository.save(billPayment);

        // Create AirtimeDto from BillPaymentDTO
        AirtimeDto airtimeDto = new AirtimeDto();
        airtimeDto.setPhoneNumber(billPaymentDTO.getPhoneNumber());
        airtimeDto.setAirtime(billPaymentDTO.getAmount());

        buyMtnAirtime(airtimeDto);

        PaymentLog paymentLog = PaymentLog.builder()
                .billPayment(billPayment)
                .logDate(LocalDateTime.now())
                .message("Airtime purchase successful")
                .status(Status.moneyOut)
                .build();
        paymentRepository.save(paymentLog);

        return billPaymentDTO;
    }

    @Override
    public List<PaymentLog> getPaymentLogs(String token, Integer accountNumber) {
        validateToken(token);
        validateUserAccountAssociation(token, accountNumber);
        return paymentRepository.findByBillPaymentAccountNumber(accountNumber);
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
                    .uri(url, account.getAccountNumber())
                    .bodyValue(account)
                    .retrieve().onStatus(HttpStatusCode::isError, clientResponse -> Mono.error
                            (new RuntimeException("Failed to update account details")))
                    .bodyToMono(Void.class).block();
        } catch (Exception e) {
            System.out.println("Error updating account details: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void buyMtnAirtime(AirtimeDto airtimedto) {
        WebClient webClient = webclientBuilder.build();
        System.out.println("here muri Bill Implementation");
        String url = "http://localhost:8084/api/v1/mtn-service/airtime";

        try {
            webClient.put()
                    .uri(url)
                    .bodyValue(airtimedto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            clientResponse -> Mono.error(
                                    new RuntimeException("Failed to update airtime")))
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("Error updating airtime: Failed to update airtime. Status code: " + e.getStatusCode() +
                    ", Response body: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to from the side of mtn", e);
        } catch (Exception e) {
            System.out.println("Error updating airtime: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


