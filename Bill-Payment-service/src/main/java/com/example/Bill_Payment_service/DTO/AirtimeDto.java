package com.example.Bill_Payment_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AirtimeDto {
    private String phoneNumber;
    private Double airtime;
    private String email;

}
