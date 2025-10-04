package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.request.OrderFilterDTO;
import com.example.backend.dto.response.OrderResponseDTO;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.model.Order;

public interface OrderService {
    List<OrderResponseDTO> findAll(OrderFilterDTO o);

    Order findOne(String id);

    String initOrder(OrderResponseDTO order);

    void holdSeats(String orderId, List<OrderSeatDTO> seatsId, Long userId);

    Order confirm(OrderResponseDTO order);

    Order update(String id, Order o);

    void delete(String id);
    
    
}
