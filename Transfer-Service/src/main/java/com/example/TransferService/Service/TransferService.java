package com.example.TransferService.Service;

import com.example.TransferService.Entity.DTO.TransferDTO;
import com.example.TransferService.Entity.DTO.TransferLogsDTO;
import com.example.TransferService.Entity.DTO.TransferResponse;
import com.example.TransferService.Entity.DTO.TransferResponseLogs;
import com.example.TransferService.Entity.TransferLogs;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransferService {

    TransferResponse createTransfer(String token, TransferDTO transferDTO);

    TransferResponseLogs createTransferLogs(TransferLogsDTO transferLogsDTO, Integer id);

    List<TransferLogs> getTransferLogsByAccount(String token, Integer accountNumber);


}
