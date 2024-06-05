package com.example.Bill_Payment_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mtn implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    private Integer id;
    private String phoneNumber;
    private Double airtime;
    private LocalDate paymentDate;

}
