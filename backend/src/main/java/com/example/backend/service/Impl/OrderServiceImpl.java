package com.example.backend.service.Impl;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.constant.SeatStatus;
import com.example.backend.dto.shared.OrderDTO;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.exception.wrapper.*;
import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import com.example.backend.repository.OrderRepository;
import com.example.backend.service.OrderService;
import com.example.backend.util.OrderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Duration HOLD_TIMEOUT = Duration.ofMinutes(15);


    public OrderServiceImpl(OrderRepository orderRepository, SimpMessagingTemplate messagingTemplate,
                            RedisTemplate<String, Object> redisTemplate) {
        this.orderRepository = orderRepository;
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Order> findAll(Order o) {
        return orderRepository.findAllWithFilter(o);
    }

    @Override
    public Order findOne(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @Scheduled(fixedRate = 5000)
    public void getCountdown() {
        log.info("Running getCountdown schedule task");
        Set<String> keys = redisTemplate.keys("orderId:*");
        log.info("Getting countdown keys {}", keys);
        if (keys.isEmpty()) return;

        for (String redisKey : keys) {
            Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
            log.info("Getting count down task for {}", redisKey);
            if (ttl > 0) {
                String orderId = redisKey.substring(redisKey.lastIndexOf(":") + 1);
                messagingTemplate.convertAndSend("/topic/order/" + orderId, Map.of(
                        "event", "TTL_SYNC",
                        "orderId", orderId,
                        "ttl", ttl
                ));
                log.info("Sending TTL_SYNC for order {} with TTL {}", orderId, ttl);
            }
        }
    }

    @Override
    public String initOrder(OrderDTO o) {
        String orderId = o.getOrderId();
        String redisKey;
        if (orderId != null && redisTemplate.hasKey(RedisKeyHelper.orderHoldKey(orderId))) {
            redisKey = RedisKeyHelper.orderHoldKey(orderId);
            redisTemplate.expire(redisKey, HOLD_TIMEOUT);

            messagingTemplate.convertAndSend("/topic/order/" + orderId, Map.of(
                            "event", "TTL_SYNC",
                            "orderId", orderId,
                            "ttl", HOLD_TIMEOUT.toSeconds()
                    )
            );
            log.info("Sending TTL_SYNC for order {} with TTL {}", orderId, HOLD_TIMEOUT.toSeconds());
            return orderId;
        }

        orderId = OrderUtils.generateOrderIds();
        redisKey = RedisKeyHelper.orderHoldKey(orderId);
        o.setOrderStatus(OrderStatus.CREATED);
        o.setOrderId(orderId);
        o.setSeats(new ArrayList<>());
        Map<String, String> orderData = Map.of(
                "userId", o.getUserId().toString(),
                "status", o.getOrderStatus().name(),
                "cinemaHallId", o.getCinemaHallId().toString(),
                "seats", "[]");

        redisTemplate.opsForHash().putAll(redisKey, orderData);
        redisTemplate.expire(redisKey, HOLD_TIMEOUT);

        messagingTemplate.convertAndSend("/topic/order/" + orderId, Map.of(
                        "event", "TTL_SYNC",
                        "orderId", orderId,
                        "ttl", HOLD_TIMEOUT.toSeconds()
                )
        );
        log.info("Sending TTL_SYNC for order {} with TTL {}", orderId, HOLD_TIMEOUT.toSeconds());
        return orderId;
    }

    public void holdSeats(String orderId, List<OrderSeatDTO> orderSeats, Long userId) {

        String redisKey = RedisKeyHelper.orderHoldKey(orderId);
        String userIdStr = (String) redisTemplate.opsForHash().get(redisKey, "userId");

        if (userIdStr == null) {
            throw new OrderNotFoundException(HttpStatus.NOT_FOUND, "Order not exist or not found");
        }

        if (!userId.equals(Long.parseLong(userIdStr))) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permisson !");
        }

        List<OrderSeat> checkSeats = orderSeats.stream().map(dto -> {
            OrderSeat seat = new OrderSeat();
            seat.setSeat(dto.getSeat());
            seat.setPrice(dto.getPrice());
            return seat;
        }).collect(Collectors.toList());

        boolean isBooked = orderRepository.findOrderSeatsExisted(checkSeats);
        if (isBooked) {
            throw new SeatUnavailableException(HttpStatus.GONE, "Some seat you choose has been booked");
        }

        for (OrderSeatDTO seat : orderSeats) {
            String lockKey = RedisKeyHelper.seatLockKey(seat.getSeat().getId());
            String existingLock =(String) redisTemplate.opsForValue().get(lockKey);
            if (existingLock != null && !existingLock.equals(redisKey)) {
                throw new SeatUnavailableException(HttpStatus.NOT_FOUND, "Seat " + seat.getSeat().getId() + " not avalable");
            }

            redisTemplate.opsForValue().set(lockKey, redisKey,HOLD_TIMEOUT);

        }
        try {

            String seatsJson = new ObjectMapper().writeValueAsString(orderSeats);
            redisTemplate.opsForHash().put(redisKey, "seats", seatsJson);
        } catch (JsonProcessingException ex) {
            throw new RedisStorageException(HttpStatus.NOT_FOUND, "Info in redis not exists or error when getting that");
        }
    }

    @Override
    @Transactional
    public Order confirm(String orderId, Long userId) {
        OrderDTO dto = getFromRedis(orderId);
        if (!dto.getUserId().equals(userId)) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permission");
        }

        List<OrderSeat> seatEntities = dto.getSeats().stream().map(s -> {
            OrderSeat os = new OrderSeat();
            os.setSeat(s.getSeat());
            os.setPrice(s.getPrice());
            os.setOrderId(orderId);
            return os;
        }).toList();
        Order o = new Order();
        o.setOrderId(orderId);
        o.setUserId(userId);
        o.markCreated();
        o.setTotalAmount(dto.getSeats().stream().map(OrderSeatDTO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        o.setCinemaHallId(dto.getCinemaHallId());
        o.setOrderStatus(OrderStatus.CONFIRMED);
        o.setPaymentMethod(PaymentMethod.CASH);
        o.setPaymentStatus(PaymentStatus.PAID);
        o.setSeats(seatEntities);
        orderRepository.insert(o);
        orderRepository.insertOrderSeat(o.getSeats());
        orderRepository.updateOrderSeat(o.getOrderId(), SeatStatus.BOOKED.name());
//	Delete Redis when save DB
        dto.getSeats().forEach(seat -> redisTemplate.delete(RedisKeyHelper.seatLockKey(seat.getSeat().getId())));

        redisTemplate.delete(RedisKeyHelper.orderHoldKey(orderId));
        return o;
    }

    @Override
    @Transactional
    public Order update(String id, Order o) {
        o.setOrderId(id);
        orderRepository.update(o);
        return o;

    }

    @Override
    public void delete(String id) {
        orderRepository.delete(id);

    }

    private OrderDTO getFromRedis(String orderId) {
        String key = RedisKeyHelper.orderHoldKey(orderId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);
        if (data.isEmpty()) {
            throw new OrderExpiredException(HttpStatus.NOT_FOUND, "Order expired or not existed");
        }

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(orderId);
        dto.setUserId(Long.parseLong((String) data.get("userId")));
        dto.setOrderStatus(OrderStatus.valueOf((String) data.get("status")));
        dto.setCinemaHallId(Long.parseLong((String) data.get("cinemaHallId")));
        try {
            List<OrderSeatDTO> seats = new ObjectMapper().readValue((String) data.get("seats"),
                    new TypeReference<>() {
                    });
            dto.setSeats(seats);

        } catch (JsonProcessingException ex) {
            throw new RedisStorageException(HttpStatus.NOT_FOUND, "Info in redis not exists or error when getting that");
        }
        return dto;
    }

}
