package com.example.ordermicroservice.controller;

import com.example.ordermicroservice.service.OrdersServiceImpl;
import com.example.ordermicroservice.model.OrderModel;
import com.example.ordermicroservice.model.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/orders")
public class OrdersController {

    @Qualifier("ordersServiceImpl")
    @Autowired
    private OrdersServiceImpl ordersServiceImpl;


    // Save a product
    @PostMapping("/save")
    public ResponseEntity<OrderModel> placeOrder(@RequestBody OrderModel orderModel) {
        OrderModel saveOrder = ordersServiceImpl.saveOrder(orderModel);
        return new ResponseEntity<>(saveOrder, OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderModel>> getAllOrders() {
        List<OrderModel> allOrders = ordersServiceImpl.getAllOrders();
        return new ResponseEntity<>(allOrders, OK);
    }

    @DeleteMapping("/removeOrder/{id}")
    public String removeOrder(@PathVariable("id") Long id) {
        ordersServiceImpl.removeOrder(id);
        return "Order with id " + id + " removed successfully";
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(
            @PathVariable Long orderId) {
        OrderResponse orderResponse = ordersServiceImpl.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponse, OK);
    }
}
