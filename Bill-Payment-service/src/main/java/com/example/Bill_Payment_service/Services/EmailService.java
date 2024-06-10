package com.example.Bill_Payment_service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String fromMail;

    public void sendEmail(String email, String subject, String messages) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(messages);

        mailSender.send(message);
    }

//    @PostConstruct
//    public void Foo() {
//
//        sendEmail("olivierrugwiro@gmail.com", "Testing", "It worked");
//    }
}
