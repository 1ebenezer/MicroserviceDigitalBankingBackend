package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.MtnReponse;
import com.example.Bill_Payment_service.DTO.MtnRequest;
import com.example.Bill_Payment_service.DTO.MtnRequest2;
import com.example.Bill_Payment_service.Entity.Mtn;
import com.example.Bill_Payment_service.Repository.MtnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MtnService {

    @Autowired
    private MtnRepository mtnRepository;

    public MtnReponse createNumber(MtnRequest mtnRequest) {

        Mtn mtn = new Mtn();
        mtn.setAirtime(mtnRequest.getAirtime() != null ? mtnRequest.getAirtime() : 0.0);
        mtn.setEmail(mtnRequest.getEmail());

        Mtn mtnaccount = mtnRepository.save(mtn);

        MtnReponse response = new MtnReponse();
        response.setPhoneNumber(mtnaccount.getPhoneNumber());
        response.setAirtime(mtnaccount.getAirtime());
        response.setEmail(mtnaccount.getEmail());

        return response;
    }

    public MtnReponse buyAirtime(MtnRequest2 mtnRequest2) {
        MtnReponse mtnReponse = new MtnReponse();
        Optional<Mtn> optionalMtn = mtnRepository.findByPhoneNumber(mtnRequest2.getPhoneNumber());
        System.out.println("here is the phone" + optionalMtn);
        if (optionalMtn.isPresent()) {
            Mtn mtn = optionalMtn.get();

            Double currentAirtime = mtn.getAirtime();
            if (currentAirtime == null) {
                currentAirtime = 0.0;
            }
            System.out.println("airtime is this:" + currentAirtime);


            mtn.setAirtime(currentAirtime + mtnRequest2.getAirtime());

            Mtn updatedPayment = mtnRepository.save(mtn);


            MtnReponse response = new MtnReponse();
            response.setPhoneNumber(updatedPayment.getPhoneNumber());
            response.setAirtime(updatedPayment.getAirtime());
            response.setEmail(updatedPayment.getEmail());

            return response;
        } else {
            throw new RuntimeException("Number not found with id: " + mtnReponse.getPhoneNumber());
        }
    }

    public List<MtnReponse> getAllPayments() {
        List<Mtn> payments = mtnRepository.findAll();
        List<MtnReponse> responseDTOs = new ArrayList<>();
        for (Mtn payment : payments) {
            MtnReponse responseDTO = new MtnReponse();
            responseDTO.setPhoneNumber(payment.getPhoneNumber());
            responseDTO.setAirtime(payment.getAirtime());
            responseDTO.setEmail(payment.getEmail());
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }
}
