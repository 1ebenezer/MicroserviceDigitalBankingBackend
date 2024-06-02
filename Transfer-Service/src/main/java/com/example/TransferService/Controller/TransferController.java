package com.example.TransferService.Controller;

import com.example.TransferService.Entity.DTO.TransferDTO;
import com.example.TransferService.Entity.DTO.TransferResponse;
import com.example.TransferService.Entity.Status;
import com.example.TransferService.Entity.TransferLogs;
import com.example.TransferService.Service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    @Autowired
    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> createTransfer(
            @RequestHeader("Authorization") String token,
            @RequestBody TransferDTO transferDTO) {
        try {
            TransferResponse response = transferService.createTransfer(token, transferDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            TransferResponse response = TransferResponse.builder()
                    .message(e.getMessage())
                    .status(Status.FAILED)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/logs/{senderAccount}")
    public ResponseEntity<List<TransferLogs>> getTransferLogsByAccountNumber(@RequestHeader("Authorization") String token,
                                                                             @PathVariable Integer senderAccount) {
        List<TransferLogs> transferLogs = transferService.getTransferLogsByAccount(token, senderAccount);
        return ResponseEntity.ok(transferLogs);
    }
}
