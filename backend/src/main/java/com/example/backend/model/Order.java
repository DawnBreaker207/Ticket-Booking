package com.example.backend.model;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Hidden
@Entity
@Table(name = "orders")
public class Order extends AbstractMappedEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_hall_id", nullable = false)
    private CinemaHall cinemaHall;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderSeat> seats = new ArrayList<>();

    public Order() {
        super();
    }

    public Order(
            String orderId,
            Long userId,
            CinemaHall cinemaHall,
            OrderStatus orderStatus,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            BigDecimal totalAmount,
            List<OrderSeat> seats) {
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

    public List<OrderSeat> getSeats() {
        return seats;
    }

    public void setSeats(List<OrderSeat> seats) {
        this.seats = seats;
    }


    @Override
    public String toString() {

        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", cinemaHall='" + cinemaHall + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", seats=" + seats + '\'' +
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
                && Objects.equals(cinemaHall, order.cinemaHall)
                && Objects.equals(orderStatus, order.orderStatus)
                && Objects.equals(paymentMethod, order.paymentMethod)
                && Objects.equals(paymentStatus, order.paymentStatus)
                && Objects.equals(totalAmount, order.totalAmount)
                && Objects.equals(seats, order.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, cinemaHall, orderStatus, paymentMethod, paymentStatus, totalAmount, seats);
    }

}
