package com.example.Bill_Payment_service.Controller;

import com.example.Bill_Payment_service.DTO.MtnReponse;
import com.example.Bill_Payment_service.DTO.MtnRequest;
import com.example.Bill_Payment_service.DTO.MtnRequest2;
import com.example.Bill_Payment_service.Entity.Mtn;
import com.example.Bill_Payment_service.Repository.MtnRepository;
import com.example.Bill_Payment_service.Services.Exception.InsufficientBalanceException;
import com.example.Bill_Payment_service.Services.MtnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mtn-service")

public class MtnController {
    @Autowired
    private MtnService mtnAirtimePaymentService;
    @Autowired
    private MtnRepository mtnRepository;

    @PostMapping
    public ResponseEntity<MtnReponse> createNumber(@RequestBody MtnRequest mtnRequest) {

        MtnReponse responseDTO = mtnAirtimePaymentService.createNumber(mtnRequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/airtime")
    public ResponseEntity<MtnReponse> buyAirtime(@RequestBody MtnRequest2 mtnRequest2) {
        try {
            MtnReponse responseDTO = mtnAirtimePaymentService.buyAirtime(mtnRequest2);
            return ResponseEntity.ok(responseDTO);
        } catch (InsufficientBalanceException ex) {
            MtnReponse response = MtnReponse.builder()
                    .message(ex.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            MtnReponse response = MtnReponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

    }

    @GetMapping
    public ResponseEntity<List<MtnReponse>> getAllPayments() {
        List<MtnReponse> responseDTOs = mtnAirtimePaymentService.getAllPayments();
        return new ResponseEntity<>(responseDTOs, HttpStatus.CREATED);
    }

    @DeleteMapping("{phoneNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String phoneNumber) {

        List<Mtn> account = mtnRepository.findAllByphoneNumber(phoneNumber);
        if (account.isEmpty()) {
            return ResponseEntity.status(404).body("Account with phone number " + phoneNumber + " not found.");
        } else if (account.size() == 1) {
            mtnRepository.delete(account.get(0));
            return ResponseEntity.ok("Account with phone number " + phoneNumber + " has been deleted successfully.");
        } else {
            mtnRepository.deleteAll(account);
            return ResponseEntity.ok("All accounts with phone number " + phoneNumber + " have been deleted successfully.");
        }
    }
}
