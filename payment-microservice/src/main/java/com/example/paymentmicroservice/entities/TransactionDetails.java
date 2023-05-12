package com.example.paymentmicroservice.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "TransactionDetails")
@Table(name = "transaction_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String paymentMode;
    private String referenceNumber;
    private Instant paymentDate;
    private String paymentStatus;
    private Long amount;

}
