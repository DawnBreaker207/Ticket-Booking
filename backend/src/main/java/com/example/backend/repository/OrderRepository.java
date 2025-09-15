package com.example.backend.repository;

import com.example.backend.dto.request.OrderFilterDTO;
import com.example.backend.dto.shared.OrderDTO;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface OrderRepository extends DAO<Order, String> {

    List<OrderDTO> findAllWithFilter(OrderFilterDTO order);

    @Override
    List<Order> findAll();

    @Override
    Optional<Order> findById(String s);

    @Override
    int insert(Order order);

    @Override
    int update(Order order);

    @Override
    void delete(String s);

    void insertOrderSeat(List<OrderSeat> seats);

    void updateOrderSeat(@Param("orderId") String orderId, @Param("orderStatus") String status);

    Optional<Order> findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId);

    boolean findOrderSeatsExisted(List<OrderSeat> seats);
}

