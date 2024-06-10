package com.example.Bill_Payment_service.Services;

import com.example.Bill_Payment_service.DTO.MtnReponse;
import com.example.Bill_Payment_service.DTO.MtnRequest;
import com.example.Bill_Payment_service.DTO.MtnRequest2;
import com.example.Bill_Payment_service.Entity.Mtn;
import com.example.Bill_Payment_service.Repository.MtnRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MtnService {
    private final MtnRepository mtnRepository;
    private final EmailService emailService;

    MtnService(MtnRepository mtnRepository, EmailService emailService) {
        this.mtnRepository = mtnRepository;
        this.emailService = emailService;
    }

    //    @Autowired
//    private MtnRepository mtnRepository;
//    @Autowired
//    private EmailService emailService;
    private static long currentNumber = 780000000L;

    public MtnReponse createNumber(MtnRequest mtnRequest) {

        String phoneNumber = generatePhoneNumber();


        Mtn mtn = new Mtn();
        mtn.setPhoneNumber(phoneNumber);
        mtn.setAirtime(mtnRequest.getAirtime() != null ? mtnRequest.getAirtime() : 0.0);
        mtn.setEmail(mtnRequest.getEmail());

        Mtn mtnaccount = mtnRepository.save(mtn);

        MtnReponse response = new MtnReponse();
        response.setPhoneNumber(mtnaccount.getPhoneNumber());
        response.setAirtime(mtnaccount.getAirtime());
        response.setEmail(mtnaccount.getEmail());
        response.setMessage("successfull creation");

        String email = response.getEmail();
        String Pnumber = response.getPhoneNumber();
        String subject = "successful Creation";
        String message = "Dear customer your new Number is " + Pnumber + " Welcome to MTN";

        emailService.sendEmail(response.getEmail(), subject, message);

        return response;
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
            response.setMessage("successfull purchase");

            String email = response.getEmail();
            Double amount = mtnRequest2.getAirtime();
            Double airtime = response.getAirtime();
            String subject = "successful Airtime";
            String message = "Dear customer your airtime purchase of " + amount + " your current balance is " + airtime + " was succesfull enjoy!";

            emailService.sendEmail(response.getEmail(), subject, message);

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
