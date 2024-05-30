package com.example.TransferService.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferLogs {
    @Id
    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer senderAccount;
    private Integer receiverAccount;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private Double amount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;
}
