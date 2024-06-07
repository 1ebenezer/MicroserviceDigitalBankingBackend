package com.example.Bill_Payment_service.Controller;

import com.example.Bill_Payment_service.DTO.BillPaymentDTO;
import com.example.Bill_Payment_service.Entity.PaymentLog;
import com.example.Bill_Payment_service.Services.BillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/billPayment")
public class BillPaymentController {

    @Autowired
    BillPaymentService billPaymentService;

    @PostMapping
    public ResponseEntity<BillPaymentDTO> makePayment(
            @RequestHeader("Authorization") String token
            , @RequestBody BillPaymentDTO BillPaymentDTO) {
        BillPaymentDTO savedPayment = billPaymentService.makePayment(token, BillPaymentDTO);
        return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
    }

    @GetMapping("/logs/{phoneNumber}")
    public ResponseEntity<List<PaymentLog>> getPaymentLogs(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer accountNumber) {
        List<PaymentLog> logs = billPaymentService.getPaymentLogs(token, accountNumber);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}
