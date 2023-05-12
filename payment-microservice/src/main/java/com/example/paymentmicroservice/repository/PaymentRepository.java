package com.example.paymentmicroservice.repository;

import com.example.paymentmicroservice.entities.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository
        extends JpaRepository<TransactionDetails, Long> {


    TransactionDetails findByOrderId(Long orderId);
}