package com.example.Bill_Payment_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentLog {
    @Id
    @GeneratedValue(generator= "uuid")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private BillPayment billPayment;
    private LocalDateTime logDate;
    private String message;
}
