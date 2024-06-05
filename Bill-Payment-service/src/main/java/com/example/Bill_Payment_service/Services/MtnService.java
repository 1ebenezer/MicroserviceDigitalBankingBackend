package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.MtnReponse;
import com.example.Bill_Payment_service.DTO.MtnRequest;
import com.example.Bill_Payment_service.Entity.Mtn;
import com.example.Bill_Payment_service.Repository.MtnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MtnService {

    @Autowired
    private MtnRepository mtnRepository;

    public MtnReponse createNumber(MtnRequest mtnRequest) {

        Mtn mtn = new Mtn();
        mtn.setPhoneNumber(mtnRequest.getPhoneNumber());
        mtn.setAirtime(mtnRequest.getAirtime());

        Mtn mtnaccount = mtnRepository.save(mtn);

        MtnReponse response = new MtnReponse();
        response.setPhoneNumber(mtnaccount.getPhoneNumber());
        response.setAirtime(mtnaccount.getAirtime());

        return response;
    }

    public MtnReponse buyAirtime(MtnRequest mtnRequest) {
        Optional<Mtn> optionalMtn = mtnRepository.findByPhoneNumber(mtnRequest.getPhoneNumber());

        if (optionalMtn.isPresent()) {
            Mtn mtn = new Mtn();
            mtn.setAirtime(mtnRequest.getAirtime());
            mtn.setPaymentDate(LocalDate.now());

            Mtn updatedPayment = mtnRepository.save(mtn);

            MtnReponse response = new MtnReponse();
            response.setPhoneNumber(updatedPayment.getPhoneNumber());
            response.setAirtime(updatedPayment.getAirtime());
            response.setPaymentDate(updatedPayment.getPaymentDate());

            return response;
        } else {
            throw new RuntimeException("Number not found with id: ");
        }
    }

    public List<MtnReponse> getAllPayments() {
        List<Mtn> payments = mtnRepository.findAll();
        List<MtnReponse> responseDTOs = new ArrayList<>();
        for (Mtn payment : payments) {
            MtnReponse responseDTO = new MtnReponse();
            responseDTO.setPhoneNumber(payment.getPhoneNumber());
            responseDTO.setAirtime(payment.getAirtime());
            responseDTO.setPaymentDate(payment.getPaymentDate());
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }
}
