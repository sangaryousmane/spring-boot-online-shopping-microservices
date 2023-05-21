package com.example.ordermicroservice.service;
import com.example.ordermicroservice.model.OrderModel;
import com.example.ordermicroservice.model.OrderResponse;

import java.util.List;

public interface OrdersService {

    OrderModel saveOrder(OrderModel order);

    List<OrderModel> getAllOrders();
    void removeOrder(Long id);

    OrderResponse getOrderDetails(Long orderId);
}
