package com.customer.CustomerService.dto;

import com.customer.CustomerService.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerDto {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String customerEmail;
    private Address address;
}
