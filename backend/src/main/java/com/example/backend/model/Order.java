package com.example.backend.model;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Hidden
public class Order extends AbstractMappedEntity {
    private String orderId;

    private Long userId;

    private Long cinemaHallId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderTime;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private List<OrderSeat> seats;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime expiredAt;

    public Order() {
        super();
    }

    public Order(
            String orderId,
            Long userId,
            Long cinemaHallId,
            LocalDateTime orderTime,
            OrderStatus orderStatus,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            BigDecimal totalAmount,
            List<OrderSeat> seats,
            LocalDateTime expiredAt) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.cinemaHallId = cinemaHallId;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.seats = seats;
        this.expiredAt = expiredAt;
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

    public Long getCinemaHallId() {
        return cinemaHallId;
    }

    public void setCinemaHallId(Long cinemaHallId) {
        this.cinemaHallId = cinemaHallId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
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

    public List<OrderSeat> getSeats() {
        return seats;
    }

    public void setSeats(List<OrderSeat> seats) {
        this.seats = seats;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {

        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", cinemaHallId='" + cinemaHallId + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", seats=" + seats + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", expiredAt='" + expiredAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Order order = (Order) obj;
        return Objects.equals(orderId, order.orderId)
                && Objects.equals(userId, order.userId)
                && Objects.equals(cinemaHallId, order.cinemaHallId)
                && Objects.equals(orderTime, order.orderTime)
                && Objects.equals(orderStatus, order.orderStatus)
                && Objects.equals(paymentMethod, order.paymentMethod)
                && Objects.equals(paymentStatus, order.paymentStatus)
                && Objects.equals(totalAmount, order.totalAmount)
                && Objects.equals(seats, order.seats)
                && Objects.equals(expiredAt, order.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, cinemaHallId, orderTime, orderStatus, paymentMethod, paymentStatus, totalAmount, seats, expiredAt);
    }

}
