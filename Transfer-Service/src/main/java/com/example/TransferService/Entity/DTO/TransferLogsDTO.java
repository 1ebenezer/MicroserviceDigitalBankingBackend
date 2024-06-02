package com.example.TransferService.Entity.DTO;

import com.example.TransferService.Entity.Status;
import com.example.TransferService.Entity.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferLogsDTO {

    private Integer Id;
    private Status status;
    private Integer receiverAccount;
    private Double amount;
    private Integer senderAccount;
    private LocalDateTime timestamp;
    private TransferType transferType;
}
