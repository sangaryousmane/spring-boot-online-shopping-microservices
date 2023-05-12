package com.example.paymentmicroservice.service;

import com.example.paymentmicroservice.entities.TransactionDetails;
import com.example.paymentmicroservice.models.PaymentMode;
import com.example.paymentmicroservice.models.PaymentRequest;
import com.example.paymentmicroservice.models.PaymentResponse;
import com.example.paymentmicroservice.repository.PaymentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Long doPayment(PaymentRequest makePayment) {
        log.info("Recording payment details{}", makePayment);

        TransactionDetails transaction = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(makePayment.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(makePayment.getOrderId())
                .referenceNumber(makePayment.getReferenceNumber())
                .amount(makePayment.getAmount())
                .build();

        log.info("Saving transaction...");
        paymentRepository.save(transaction);

        log.info("Transaction completed with id: {}", transaction.getId());

        return transaction.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(Long orderId) {
        log.info("Getting payment details for the order id: {}", orderId);

        TransactionDetails transactionDetails =
                paymentRepository.findByOrderId(orderId);

        return PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .OrderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .build();
    }
}
