package com.example.productmicroservice.entities;

import lombok.*;


import javax.persistence.*;

@Entity(name = "Product")
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "QUANTITY")
    private Long quantity;
}
