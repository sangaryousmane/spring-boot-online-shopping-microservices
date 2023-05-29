package com.example.ordermicroservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @NoArgsConstructor
@AllArgsConstructor @Data
public class CustomerRequest {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String customerEmail;
    private Address address;


    @Builder @NoArgsConstructor
    @AllArgsConstructor @Data
    static class Address {
        private String city;
        private String state;
        private String postalAddress;
        private String country;
    }
}

