package com.customer.CustomerService.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Customer")
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "customers",
        uniqueConstraints = @UniqueConstraint(
        name = "customer_uniq_email", columnNames = {"customerEmail"}))
public class Customer {

    @Id
    @SequenceGenerator(
            name="customerSequence",
            sequenceName = "customerSequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customerSequence")
    private Long customerId;
    private String firstName;
    private String lastName;
    private String customerEmail;
    @Embedded
    private Address address;
}
