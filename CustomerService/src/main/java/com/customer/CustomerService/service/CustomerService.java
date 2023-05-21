package com.customer.CustomerService.service;

import com.customer.CustomerService.dto.CustomerDto;
import com.customer.CustomerService.entity.Customer;
import org.bouncycastle.asn1.ocsp.ResponderID;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    String deleteCustomer(Customer customer);

    CustomerDto makeOrder(CustomerDto customerId);

    Map<String, Boolean> updateDetails(CustomerDto customerDto, Long id);
}
