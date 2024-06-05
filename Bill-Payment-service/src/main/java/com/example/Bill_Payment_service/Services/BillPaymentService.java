package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.BillPaymentDTO;
import org.springframework.stereotype.Service;

@Service
public interface BillPaymentService {

    BillPaymentDTO makePayment(String token, BillPaymentDTO billPaymentDTO);

}
