package com.example.paymentmicroservice.controller;

import com.example.paymentmicroservice.models.PaymentRequest;
import com.example.paymentmicroservice.models.PaymentResponse;
import com.example.paymentmicroservice.service.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/makePayment")
    public ResponseEntity<Long> doPayment(
            @RequestBody PaymentRequest paymentRequest) {
        Long paid = paymentService.doPayment(paymentRequest);
        return new ResponseEntity<>(paid, OK);
    }

    // Get payment details by order id
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(
            @PathVariable Long orderId) {
        return new ResponseEntity<>(paymentService.getPaymentDetailsByOrderId(orderId), OK);
    }


}
