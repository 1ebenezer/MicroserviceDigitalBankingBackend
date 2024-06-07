package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.BillPaymentDTO;
import com.example.Bill_Payment_service.Entity.PaymentLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BillPaymentService {

    BillPaymentDTO makePayment(String token, BillPaymentDTO BillPaymentDTO);

    List<PaymentLog> getPaymentLogs(String token, Integer accountNumber);


}
