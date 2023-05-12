package com.example.ApiGatewayService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/orderServiceFallback")
    public String orderServiceFallback(){
        return "Order service is down" +
                " or can't process your request now.";
    }

    @GetMapping("/paymentServiceFallback")
    public String paymentServiceFallback(){
        return "payment service is down," +
                " please try again later.";
    }

    @GetMapping("/productServiceFallback")
    public String productServiceFallback(){
        return "product service is faulty, " +
                "can't access your request now.";
    }
}
