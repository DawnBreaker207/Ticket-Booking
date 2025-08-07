package com.example.backend.service.Impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.dto.OrderDTO;
import com.example.backend.dto.OrderSeatDTO;
import com.example.backend.exception.wrapper.OrderExpiredException;
import com.example.backend.exception.wrapper.OrderNotFoundException;
import com.example.backend.exception.wrapper.SeatUnavailableException;
import com.example.backend.helper.OrderMappingHelper;
import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.model.Order;
import com.example.backend.repository.Impl.OrderRepositoryImpl;
import com.example.backend.service.OrderService;
import com.example.backend.util.OrderUtils;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepositoryImpl orderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration HOLD_TIMEOUT = Duration.ofMinutes(15);

    public OrderServiceImpl(OrderRepositoryImpl orderRepository, RedisTemplate<String, Object> redisTemplate) {
	this.orderRepository = orderRepository;
	this.redisTemplate = redisTemplate;

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
    public String create(OrderDTO o) {
	for (OrderSeatDTO seat : o.getSeats()) {
	    if (isSeatLocked(seat.getSeatId())) {
		throw new SeatUnavailableException("Seat " + seat.getSeatId() + " is already locked");
	    }
	}
	String holdKey = RedisKeyHelper.orderHoldKey(OrderUtils.generateOrderIds());

	for (OrderSeatDTO seat : o.getSeats()) {
	    String seatKey = RedisKeyHelper.seatLockKey(seat.getSeatId());
	    redisTemplate.opsForValue().set(seatKey, holdKey, HOLD_TIMEOUT);

	}
	redisTemplate.opsForValue().set(holdKey, o, HOLD_TIMEOUT);
	return holdKey;
    }

    @Override
    public void confirm(String holdKey) {
	OrderDTO request = (OrderDTO) redisTemplate.opsForValue().get(RedisKeyHelper.orderHoldKey(holdKey));
	if (request == null) {
	    throw new OrderExpiredException("Order expired !");
	}

	Order o = OrderMappingHelper.map(request);
	LocalDateTime now = LocalDateTime.now();
	o.setOrderTime(now);
	o.setCreatedAt(now);
	o.setExpiredAt(now.plusMinutes(15));
	o.setOrderStatus(OrderStatus.CONFIRMED);
	o.setPaymentMethod(PaymentMethod.CASH);
	o.setPaymentStatus(PaymentStatus.PAID);
	orderRepository.save(o);

//	Delete Redis when save DB
	for (OrderSeatDTO seat : request.getSeats()) {
	    String seatKey = RedisKeyHelper.seatLockKey(seat.getSeatId());
	    redisTemplate.delete(seatKey);
	}
	redisTemplate.delete(holdKey);
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

    boolean isSeatLocked(Long seatId) {
	String key = "seat:locked:" + seatId;
	return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrder() {
	log.info("### Cron check update order success! ###");
	List<Order> expiredOrder = orderRepository.findPendingOrderExpired();
	orderRepository.updateExpiredOrder();
    }

}
