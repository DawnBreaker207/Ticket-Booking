package com.example.backend.model;

import com.example.backend.constant.OrderStatus;
import com.example.backend.constant.PaymentMethod;
import com.example.backend.constant.PaymentStatus;
import io.swagger.v3.oas.annotations.Hidden;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Hidden
@Alias("Order")
public class Order extends AbstractMappedEntity {
    private String orderId;

    private Long userId;

    private Long cinemaHallId;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private List<OrderSeat> seats = new ArrayList<>();

    public Order() {
        super();
    }

    public Order(
            String orderId,
            Long userId,
            Long cinemaHallId,
            OrderStatus orderStatus,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            BigDecimal totalAmount,
            List<OrderSeat> seats) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.cinemaHallId = cinemaHallId;
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

    public Long getCinemaHallId() {
        return cinemaHallId;
    }

    public void setCinemaHallId(Long cinemaHallId) {
        this.cinemaHallId = cinemaHallId;
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
                ", cinemaHallId='" + cinemaHallId + '\'' +
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
                && Objects.equals(cinemaHallId, order.cinemaHallId)
                && Objects.equals(orderStatus, order.orderStatus)
                && Objects.equals(paymentMethod, order.paymentMethod)
                && Objects.equals(paymentStatus, order.paymentStatus)
                && Objects.equals(totalAmount, order.totalAmount)
                && Objects.equals(seats, order.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, cinemaHallId, orderStatus, paymentMethod, paymentStatus, totalAmount, seats);
    }

}
