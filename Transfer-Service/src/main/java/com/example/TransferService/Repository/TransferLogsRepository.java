package com.example.TransferService.Repository;

import com.example.TransferService.Entity.TransferLogs;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferLogsRepository extends JpaRepository<TransferLogs, Integer> {
    List<TransferLogs> findByAccountNumber(Integer accountNumber);

}
