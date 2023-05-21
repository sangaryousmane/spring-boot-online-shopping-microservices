package com.example.ordermicroservice.entities;


import lombok.*;
import javax.persistence.*;
import java.time.Instant;


@Entity(name = "Orders")
@Getter @Setter
@NoArgsConstructor
@Builder @AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Instant orderDate;
    private Long amount;
    private String orderState;

    @Column(name = "customer_id")
    private Long customerId;


    public Orders(Long productId, Long quantity, Instant orderDate,
                  Long amount, String orderState) {
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.amount = amount;
        this.orderState = orderState;
    }
}
