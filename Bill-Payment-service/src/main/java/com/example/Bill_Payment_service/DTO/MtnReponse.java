package com.example.Bill_Payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MtnReponse {

    private String phoneNumber;
    private Double airtime;
    private LocalDate paymentDate;
    private String status;

}
