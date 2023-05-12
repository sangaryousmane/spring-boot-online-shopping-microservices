package com.example.ordermicroservice.repository;

import com.example.ordermicroservice.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}