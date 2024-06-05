package com.example.Bill_Payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BillPaymentDTO {
    private Double amount;
    private String biller;
    private Integer accountNumber;
    private LocalDateTime paymentDate;
    private String Status;
}

