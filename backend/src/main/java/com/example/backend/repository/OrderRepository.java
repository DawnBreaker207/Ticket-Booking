package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;

public interface OrderRepository extends DAO<Order> {
    Optional<Order> findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId);
    boolean findOrderSeatsExisted(List<OrderSeat> seats);
}
