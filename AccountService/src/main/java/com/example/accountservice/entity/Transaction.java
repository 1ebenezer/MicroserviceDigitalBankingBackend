package com.example.accountservice.entity;

import com.example.accountservice.entity.Enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Double amount;
    private Type type;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "accNumber")
    private Account account;

}
