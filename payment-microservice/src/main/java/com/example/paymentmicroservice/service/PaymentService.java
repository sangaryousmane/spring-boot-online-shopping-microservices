package com.example.paymentmicroservice.service;

import com.example.paymentmicroservice.models.PaymentRequest;
import com.example.paymentmicroservice.models.PaymentResponse;

public interface PaymentService {

    Long doPayment(PaymentRequest makePayment);

    PaymentResponse getPaymentDetailsByOrderId(Long orderId);
}
