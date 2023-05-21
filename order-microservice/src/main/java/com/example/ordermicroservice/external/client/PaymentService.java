package com.example.ordermicroservice.external.client;
import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import com.example.ordermicroservice.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE", url = "http://localhost:8080/v1/payment")
public interface PaymentService {

    // TODO: Method for handling payment request
    @PostMapping("/makePayment")
    ResponseEntity<Long> doPayment(
            @RequestBody PaymentRequest paymentRequest);

    default ResponseEntity<Void> fallback(Exception e){
        throw new CustomFeignErrorResponseException(
                "payment service is not available",
                "SERVICE IS UNAVAILABLE", 500);
    }
}
