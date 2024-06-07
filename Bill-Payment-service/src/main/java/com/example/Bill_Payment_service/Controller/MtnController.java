package com.example.Bill_Payment_service.Controller;

import com.example.Bill_Payment_service.DTO.MtnReponse;
import com.example.Bill_Payment_service.DTO.MtnRequest;
import com.example.Bill_Payment_service.DTO.MtnRequest2;
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


    @PostMapping
    public ResponseEntity<MtnReponse> createNumber(@RequestBody MtnRequest mtnRequest) {

        MtnReponse responseDTO = mtnAirtimePaymentService.createNumber(mtnRequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/airtime")
    public ResponseEntity<MtnReponse> buyAirtime(@RequestBody MtnRequest2 mtnRequest2) {

        MtnReponse responseDTO = mtnAirtimePaymentService.buyAirtime(mtnRequest2);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<MtnReponse>> getAllPayments() {
        List<MtnReponse> responseDTOs = mtnAirtimePaymentService.getAllPayments();
        return new ResponseEntity<>(responseDTOs, HttpStatus.CREATED);
    }
}
