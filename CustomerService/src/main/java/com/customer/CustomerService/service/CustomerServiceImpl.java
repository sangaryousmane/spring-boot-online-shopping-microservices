package com.customer.CustomerService.service;

import com.customer.CustomerService.dto.CustomerDto;
import com.customer.CustomerService.entity.Address;
import com.customer.CustomerService.entity.Customer;
import com.customer.CustomerService.exceptions.CustomerNotFoundException;
import com.customer.CustomerService.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(
                customer -> {
                    CustomerDto customerDto = new CustomerDto();
                    BeanUtils.copyProperties(customer, customerDto);
                    return customerDto;
                }).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("customer with Id " + id + " not found"));
        CustomerDto customerDto=new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Override
    public String deleteCustomer(Customer customer) {

        try{
            log.info("Deleting customer with id {}", customer.getCustomerId());
            customerRepository.delete(customer);
            log.info("Customer with Id {} has been deleted",
                    customer.getCustomerId());
            return "Deleted";
        }
        catch (CustomerNotFoundException e){
            log.error("Unable to delete customer...");
            throw new CustomerNotFoundException("Customer with Id " + customer.getCustomerId() + " not found");
        }
    }

    @Override
    public CustomerDto makeOrder(CustomerDto customerId) {
       Customer customer = new Customer();
       BeanUtils.copyProperties(customerId, customer);
       customerRepository.save(customer);
       return customerId;
    }

    @Override
    public Map<String, Boolean> updateDetails(CustomerDto customerDto, Long id) {
        Boolean isCustomerAvailable = customerRepository.existsById(id);
        if(isCustomerAvailable) {
            Customer customer = Customer.builder()
                    .customerEmail(customerDto.getCustomerEmail())
                    .firstName(customerDto.getFirstName())
                    .address(new Address(customerDto.getAddress().getCity(), customerDto.getAddress().getCountry()))
                    .build();
            log.info("Saving customer....");
            customerRepository.save(customer);
        }
        else {
            log.error("Sorry, customer not found.");
            throw new CustomerNotFoundException("Customer not found");
        }
        Map<String, Boolean> isUpdated=new HashMap<>();
        isUpdated.put("Updated", isCustomerAvailable);
        return isUpdated;
    }
}
