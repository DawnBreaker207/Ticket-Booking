package com.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.backend.config.OrderStatus;
import com.example.backend.config.PaymentMethod;
import com.example.backend.config.PaymentStatus;

public class OrderDTO {
    private Long userId;

    private Long cinemaHallId;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private List<OrderSeatDTO> seats;

    public OrderDTO() {

    }

    public OrderDTO(Long userId, Long cinemaHallId, OrderStatus orderStatus, PaymentMethod paymentMethod,
	    PaymentStatus paymentStatus, BigDecimal totalAmount, List<OrderSeatDTO> seats) {
	this.userId = userId;
	this.cinemaHallId = cinemaHallId;
	this.orderStatus = orderStatus;
	this.paymentMethod = paymentMethod;
	this.paymentStatus = paymentStatus;
	this.totalAmount = totalAmount;
	this.seats = seats;
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

    public List<OrderSeatDTO> getSeats() {
	return seats;
    }

    public void setSeats(List<OrderSeatDTO> seats) {
	this.seats = seats;
    }

}
