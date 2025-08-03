package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.OrderDTO;
import com.example.backend.model.Order;

public interface OrderService {
    List<Order> findAll();

    Order findOne(String id);

    Order save(OrderDTO o);

    Order update(String id, Order o);

    void delete(String id);
}
