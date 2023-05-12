package com.example.paymentmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class PaymentRequest {

    private Long orderId;
    private Long amount;
    private String referenceNumber;
    private PaymentMode paymentMode;

    public PaymentRequest() {
    }
}
