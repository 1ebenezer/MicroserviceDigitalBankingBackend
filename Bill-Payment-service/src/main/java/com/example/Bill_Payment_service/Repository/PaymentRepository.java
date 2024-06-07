package com.example.Bill_Payment_service.Repository;

import com.example.Bill_Payment_service.Entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentLog, Integer> {
    List<PaymentLog>findByBillPaymentAccountNumber(Integer accountNumber);
}
