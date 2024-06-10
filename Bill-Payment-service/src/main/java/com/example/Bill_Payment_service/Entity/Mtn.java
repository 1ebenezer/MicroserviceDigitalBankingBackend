package com.example.Bill_Payment_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mtn implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    private Integer id;
    private String phoneNumber;
    private Double airtime;
    private String email;

}
