package com.example.ordermicroservice.external.client;

import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import com.example.ordermicroservice.model.CustomerRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "CUSTOMER-SERVICE", url = "http://localhost:9000/v1/customers")
public interface CustomerService {

    @GetMapping("/{id}")
    CustomerRequest getCustomerDetails(@PathVariable Long id);

    default ResponseEntity<Void> fallback() {
        throw new CustomFeignErrorResponseException(
                "Customer service not responding",
                "SERVICE NOT AVAILABLE", 500);
    }
}
