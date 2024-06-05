package com.example.Bill_Payment_service.Repository;

import com.example.Bill_Payment_service.Entity.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Integer> {

}
