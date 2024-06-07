package com.example.Bill_Payment_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@Data
@Builder
public class Mtn implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    private Integer id;
    private String phoneNumber;
    private Double airtime;
    private String email;

    private static long currentNumber = 780000000L;

    public Mtn() {

        this.phoneNumber = generatePhoneNumber();
    }

    private String generatePhoneNumber() {
        if (currentNumber > 789999999L) {
            currentNumber = 790000000L;
        }
        String prefix = String.valueOf(currentNumber).substring(0, 3);
        long number = currentNumber % 10000000;
        currentNumber++;
        return "0" + prefix + String.format("%07d", number);
    }


}
