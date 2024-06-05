package com.example.Bill_Payment_service.Repository;

import com.example.Bill_Payment_service.Entity.Mtn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MtnRepository extends JpaRepository<Mtn, Integer> {
    Optional<Mtn> findByPhoneNumber(String phone);
}
