package com.example.TransferService.Service;

import com.example.TransferService.Entity.DTO.TransferDTO;
import com.example.TransferService.Entity.DTO.TransferLogsDTO;
import com.example.TransferService.Entity.DTO.TransferResponse;
import com.example.TransferService.Entity.DTO.TransferResponseLogs;
import com.example.TransferService.Entity.TransferLogs;

import java.util.List;


public interface TransferService {

   TransferResponse createTransfer(String token, TransferDTO transferDTO);
   TransferResponseLogs createTransferLogs (TransferLogsDTO transferLogsDTO, Integer id);

    List<TransferLogs> getTransferLogsBySender(Integer senderAccount);


}
