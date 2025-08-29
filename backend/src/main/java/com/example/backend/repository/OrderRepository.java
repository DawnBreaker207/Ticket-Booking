package com.example.backend.repository;

import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface OrderRepository extends DAO<Order, String> {

    List<Order> findAllWithFilter(Order order);

    @Override
    List<Order> findAll();

    @Override
    Optional<Order> findById(String s);

    @Override
    int insert(Order order);

    @Override
    int update(Order order);

    default Order save(Order input) {
        if (input.getOrderId() == null) {
            insert(input);
        } else {
            update(input);
        }
        return input;
    }

    @Override
    void delete(String s);


    Optional<Order> findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId);

    boolean findOrderSeatsExisted(List<OrderSeat> seats);
}

