package com.example.backend.dto.request;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.dto.shared.OrderSeatDTO;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.CinemaHall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderRequestDTO extends AbstractMappedEntity {
    private String orderId;

    private Long userId;

    private CinemaHall cinemaHall;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private List<OrderSeatDTO> seats = new ArrayList<>();

    public OrderRequestDTO() {
        super();
    }

    public OrderRequestDTO(String orderId, Long userId, CinemaHall cinemaHall, OrderStatus orderStatus,
                    PaymentMethod paymentMethod, PaymentStatus paymentStatus, BigDecimal totalAmount,
                    List<OrderSeatDTO> seats) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.cinemaHall = cinemaHall;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.seats = new ArrayList<>(seats);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public void setCinemaHall(CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderSeatDTO> getSeats() {
        return new ArrayList<>(seats);
    }

    public void setSeats(List<OrderSeatDTO> seats) {
        this.seats = new ArrayList<>(seats);
    }
}
