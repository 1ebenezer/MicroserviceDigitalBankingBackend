package com.example.TransferService.Entity.DTO;

import com.example.TransferService.Entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {
    private Double amount;
    private String message;
    private Status status;

}
