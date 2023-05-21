package com.customer.CustomerService.controller;

import com.customer.CustomerService.dto.CustomerDto;
import com.customer.CustomerService.entity.Customer;
import com.customer.CustomerService.service.CustomerServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/customers")
public class CustomerV1Controller {

    @Qualifier("customerServiceImpl")
    private final CustomerServiceImpl customerServiceImpl;

    public CustomerV1Controller(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping("/makeOrder")
    public ResponseEntity<CustomerDto> makeOrder(@RequestBody CustomerDto customerDto) {
        return ok(customerServiceImpl.makeOrder(customerDto));
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerServiceImpl.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerDetails(@PathVariable Long id) {
        return customerServiceImpl.getCustomerById(id);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(Customer customer, @PathVariable Long id) {
        CustomerDto customerById = customerServiceImpl.getCustomerById(id);
        if (customerById != null) {
            BeanUtils.copyProperties(customerById, customer);
            customerServiceImpl.deleteCustomer(customer);
        }
        return ResponseEntity.ok().body(customerById);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Boolean>> updateCustomer(
            @RequestBody CustomerDto customerDto, @PathVariable Long id) {
        Map<String, Boolean> isUpdated = customerServiceImpl.updateDetails(customerDto, id);
        return ResponseEntity.ok().body(isUpdated);
    }
}
