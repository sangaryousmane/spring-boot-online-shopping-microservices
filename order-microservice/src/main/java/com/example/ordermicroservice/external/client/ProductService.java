package com.example.ordermicroservice.external.client;
import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8081/v1/products")
public interface ProductService {

    // Method for handling the reduction of quantities
    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") Long productId,
            @RequestParam Long quantity);


    default void fallback(Exception e){
        throw new CustomFeignErrorResponseException(
                "Product service not responding",
                "SERVICE NOT AVAILABLE", 500);
    }
}
