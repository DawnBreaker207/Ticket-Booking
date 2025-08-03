package com.example.backend.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.backend.dto.OrderDTO;
import com.example.backend.exception.OrderNotFoundException;
import com.example.backend.helper.OrderMappingHelper;
import com.example.backend.model.Order;
import com.example.backend.repository.Impl.OrderRepositoryImpl;
import com.example.backend.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryImpl orderRepository;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepositoryImpl orderRepository) {
	this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
	return orderRepository.findAll();
    }

    @Override
    public Order findOne(String id) {
	return orderRepository.findOne(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    public Order save(OrderDTO o) {

	Order entity = OrderMappingHelper.map(o);

//	if (orderRepository.findOrderSeatsExisted(entity.getSeats())) {
//	    throw new SeatUnavailableException("One of more seat is already locked");
//	}

	LocalDateTime now = LocalDateTime.now();
	entity.setOrderTime(now);
	entity.setCreatedAt(now);
	entity.setExpiredAt(now.plusMinutes(5));
	return orderRepository.save(entity);
    }

    @Override
    public Order update(String id, Order o) {
	o.setOrderId(id);
	return orderRepository.update(o);
    }

    @Override
    public void delete(String id) {
	orderRepository.delete(id);

    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrder() {
	log.info("### Cron check update order success! ###");
//	List<Order> expiredOrder = orderRepository.findPendingOrderExpired();
	orderRepository.updateExpiredOrder();
    }

}
