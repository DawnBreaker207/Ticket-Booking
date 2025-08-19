package com.example.backend.service.Impl;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.dto.shared.OrderDTO;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.exception.wrapper.*;
import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import com.example.backend.repository.Impl.OrderRepositoryImpl;
import com.example.backend.service.OrderService;
import com.example.backend.util.OrderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String initOrder(OrderDTO o) {
	String orderId = OrderUtils.generateOrderIds();
	String redisKey = RedisKeyHelper.orderHoldKey(orderId);

	o.setOrderStatus(OrderStatus.CREATED);
	o.setOrderId(orderId);
	o.setSeats(new ArrayList<>());
	Map<String, String> orderData = Map.of("userId", o.getUserId().toString(), "status", o.getOrderStatus().name(),
		"cinemaHallId", o.getCinemaHallId().toString(), "seats", "[]");

	redisTemplate.opsForHash().putAll(redisKey, orderData);
	redisTemplate.expire(redisKey, HOLD_TIMEOUT);
	return orderId;
    }

    public void holdSeats(String orderId, List<OrderSeatDTO> orderSeats, Long userId) {

	String redisKey = RedisKeyHelper.orderHoldKey(orderId);
	String userIdStr = (String) redisTemplate.opsForHash().get(redisKey, "userId");

	if (userIdStr == null) {
	    throw new OrderNotFoundException("Order not exist or not found");
	}

	if (!userId.equals(Long.parseLong(userIdStr))) {
	    throw new ForbiddenPermissionException("You don't have permisson !");
	}

	List<OrderSeat> checkSeats = orderSeats.stream().map(dto -> {
	    OrderSeat seat = new OrderSeat();
	    seat.setSeatId(dto.getSeatId());
	    seat.setPrice(dto.getPrice());
	    return seat;
	}).collect(Collectors.toList());

	boolean isBooked = orderRepository.findOrderSeatsExisted(checkSeats);
	if (isBooked) {
	    throw new SeatUnavailableException("Some seat you choose has been booked");
	}

	for (OrderSeatDTO seat : orderSeats) {

	    Boolean locked = redisTemplate.opsForValue().setIfAbsent(RedisKeyHelper.seatLockKey(seat.getSeatId()),
		    RedisKeyHelper.orderHoldKey(orderId), HOLD_TIMEOUT);
	    if (Boolean.FALSE.equals(locked)) {
		throw new SeatUnavailableException("Seat " + seat.getSeatId() + " not avalable");
	    }

	}
	try {

	    String seatsJson = new ObjectMapper().writeValueAsString(orderSeats);
	    redisTemplate.opsForHash().put(redisKey, "seats", seatsJson);
	} catch (JsonProcessingException ex) {
	    throw new RedisStorageException("Info in redis not exists or error when getting that");
	}
    }

    @Override
    public Order confirm(String orderId, Long userId) {
	OrderDTO dto = getFromRedis(orderId);
	if (!dto.getUserId().equals(userId)) {
	    throw new ForbiddenPermissionException("You don't have permission");
	}

	List<OrderSeat> seatEntities = dto.getSeats().stream().map(s -> {
	    OrderSeat os = new OrderSeat();
	    os.setSeatId(s.getSeatId());
	    os.setPrice(s.getPrice());
	    os.setOrderId(orderId);
	    return os;
	}).toList();

	LocalDateTime now = LocalDateTime.now();
	Order o = new Order();
	o.setOrderId(orderId);
	o.setUserId(userId);
	o.setOrderTime(now);
    o.markCreated();
	o.setTotalAmount(dto.getSeats().stream().map(OrderSeatDTO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
	o.setCinemaHallId(dto.getCinemaHallId());
	o.setExpiredAt(now.plusMinutes(15));
	o.setOrderStatus(OrderStatus.CONFIRMED);
	o.setPaymentMethod(PaymentMethod.CASH);
	o.setPaymentStatus(PaymentStatus.PAID);
	o.setSeats(seatEntities);
	orderRepository.save(o);

//	Delete Redis when save DB
	dto.getSeats().forEach(seat -> redisTemplate.delete(RedisKeyHelper.seatLockKey(seat.getSeatId())));

	redisTemplate.delete(RedisKeyHelper.orderHoldKey(orderId));
	return o;
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

    private OrderDTO getFromRedis(String orderId) {
	String key = RedisKeyHelper.orderHoldKey(orderId);
	Map<Object, Object> data = redisTemplate.opsForHash().entries(key);
	if (data.isEmpty()) {
	    throw new OrderExpiredException("Order expired or not existed");
	}

	OrderDTO dto = new OrderDTO();
	dto.setOrderId(orderId);
	dto.setUserId(Long.parseLong((String) data.get("userId")));
	dto.setOrderStatus(OrderStatus.valueOf((String) data.get("status")));
	dto.setCinemaHallId(Long.parseLong((String) data.get("cinemaHallId")));
	try {
	    List<OrderSeatDTO> seats = new ObjectMapper().readValue((String) data.get("seats"),
		    new TypeReference<List<OrderSeatDTO>>() {
		    });
	    dto.setSeats(seats);

	} catch (JsonProcessingException ex) {
	    throw new RedisStorageException("Info in redis not exists or error when getting that");
	}
	return dto;
    }

}
