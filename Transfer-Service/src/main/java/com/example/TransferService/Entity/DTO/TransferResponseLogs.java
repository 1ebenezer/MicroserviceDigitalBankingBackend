package com.example.TransferService.Entity.DTO;

import com.example.TransferService.Entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseLogs {
    private Double amount;
    private String message;
    private Status status;
    private Integer receiverAccount;
    private Integer senderAccount;
    private LocalDateTime time;
}
