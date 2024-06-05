package com.example.Bill_Payment_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BillPayment implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    private Integer id;
    private Integer accountNumber;
    private Double amount;
    private String biller;
    private LocalDateTime paymentDate;
    private Status Status;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<PaymentLog> paymentLogs;

}
