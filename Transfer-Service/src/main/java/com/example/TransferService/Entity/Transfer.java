package com.example.TransferService.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfer implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    private Integer id;
    private Integer senderAccount;
    private Integer receiverAccount;
    private Double amount;
    private LocalDateTime timestamp;
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferLogs> transferLogs;
}
