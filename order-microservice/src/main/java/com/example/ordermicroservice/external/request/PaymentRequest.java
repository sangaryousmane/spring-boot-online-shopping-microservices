package com.example.ordermicroservice.external.request;

import com.example.ordermicroservice.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
