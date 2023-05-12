package com.example.ordermicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderModel {

    private Integer orderId;
    private Long productId;
    private Long quantity;
    private Instant orderDate;
    private Long amount;
    private String orderState;
    private PaymentMode paymentMode;
}
