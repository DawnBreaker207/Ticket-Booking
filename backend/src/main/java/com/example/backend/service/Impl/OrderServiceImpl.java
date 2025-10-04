package com.example.backend.service.Impl;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.SeatStatus;
import com.example.backend.dto.request.OrderFilterDTO;
import com.example.backend.dto.response.OrderResponseDTO;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.exception.wrapper.*;
import com.example.backend.helper.OrderMappingHelper;
import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.model.CinemaHall;
import com.example.backend.model.Order;
import com.example.backend.model.OrderSeat;
import com.example.backend.repository.OrderRepository;
import com.example.backend.service.OrderService;
import com.example.backend.util.OrderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);


    private final OrderRepository orderRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final SimpMessagingTemplate messagingTemplate;

    private static final Duration HOLD_TIMEOUT = Duration.ofMinutes(15);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<OrderResponseDTO> findAll(OrderFilterDTO o) {
        List<Order> orders = orderRepository.findAllWithFilter(o);
        return orders.stream().map(OrderMappingHelper::map).toList();
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
    public String initOrder(OrderResponseDTO o) {
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
                "cinemaHallId", o.getCinemaHall().getId().toString(),
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
        }).toList();

        boolean isBooked = orderRepository.findOrderSeatsExisted(checkSeats.stream().map(OrderSeat::getId).toList());
        if (isBooked) {
            throw new SeatUnavailableException(HttpStatus.GONE, "Some seat you choose has been booked");
        }

        for (OrderSeatDTO seat : orderSeats) {
            String lockKey = RedisKeyHelper.seatLockKey(seat.getSeat().getId());
            String existingLock = (String) redisTemplate.opsForValue().get(lockKey);
            if (existingLock != null && !existingLock.equals(redisKey)) {
                throw new SeatUnavailableException(HttpStatus.NOT_FOUND, "Seat " + seat.getSeat().getId() + " not avalable");
            }

            redisTemplate.opsForValue().set(lockKey, redisKey, HOLD_TIMEOUT);

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
    public Order confirm(OrderResponseDTO order) {
        OrderResponseDTO dto = getFromRedis(order.getOrderId());
        if (!dto.getUserId().equals(order.getUserId())) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permission");
        }

        List<OrderSeat> seatEntities = dto.getSeats().stream().map(s -> {
            OrderSeat os = new OrderSeat();
            os.setSeat(s.getSeat());
            os.setPrice(s.getPrice());
            os.setOrder(OrderMappingHelper.map(order));
            return os;
        }).toList();
        Order o = new Order();
        o.setOrderId(order.getOrderId());
        o.setUserId(order.getUserId());
        o.markCreated();
        o.setTotalAmount(dto.getSeats().stream().map(OrderSeatDTO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        o.setCinemaHall(dto.getCinemaHall());
        o.setOrderStatus(order.getOrderStatus());
        o.setPaymentMethod(order.getPaymentMethod());
        o.setPaymentStatus(order.getPaymentStatus());
        o.setSeats(seatEntities);
        orderRepository.save(o);
        o.getSeats().forEach(orderRepository::insertOrderSeat);
        orderRepository.updateOrderSeat(o.getOrderId(), SeatStatus.BOOKED.name());
//	Delete Redis when save DB
        dto.getSeats().forEach(seat -> redisTemplate.delete(RedisKeyHelper.seatLockKey(seat.getSeat().getId())));

        redisTemplate.delete(RedisKeyHelper.orderHoldKey(order.getOrderId()));
        return o;
    }

    @Override
    @Transactional
    public Order update(String id, Order o) {
        o.setOrderId(id);
        orderRepository.save(o);
        return o;

    }

    @Override
    public void delete(String id) {
        orderRepository.delete(id);

    }

    private OrderResponseDTO getFromRedis(String orderId) {
        String key = RedisKeyHelper.orderHoldKey(orderId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);
        if (data.isEmpty()) {
            throw new OrderExpiredException(HttpStatus.NOT_FOUND, "Order expired or not existed");
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(orderId);
        dto.setUserId(Long.parseLong((String) data.get("userId")));
        dto.setOrderStatus(OrderStatus.valueOf((String) data.get("status")));
        dto.setCinemaHall(entityManager.getReference(CinemaHall.class, Long.parseLong((String) data.get("cinemaHallId"))));
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
