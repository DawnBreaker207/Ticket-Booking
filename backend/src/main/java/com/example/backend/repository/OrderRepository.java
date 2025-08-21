package com.example.backend.repository;

import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends DAO<Order> {
    Optional<Order> findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId);

    boolean findOrderSeatsExisted(List<OrderSeat> seats);
}
