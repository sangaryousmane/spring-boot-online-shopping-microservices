package com.customer.CustomerService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String city;
    private String state;
    private String postalAddress;
    private String country;

    public Address(String city, String country) {
        this.city = city;
        this.country = country;
    }
}
