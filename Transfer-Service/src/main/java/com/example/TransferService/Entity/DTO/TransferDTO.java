package com.example.TransferService.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor


public class TransferDTO {

    private Integer senderAccount;
    private Integer receiverAccount;
    private Double amount;

}
