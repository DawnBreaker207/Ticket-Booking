package com.example.backend.dto.shared;

import com.example.backend.model.Seat;

import java.math.BigDecimal;

public class OrderSeatDTO {
    private Seat seat;
    private BigDecimal price;

    public OrderSeatDTO() {

    }

    public OrderSeatDTO(OrderSeatDTO orderSeat) {
        this.seat = orderSeat.seat;
        this.price = orderSeat.price;
    }

    public OrderSeatDTO(Seat seatId, BigDecimal price) {
        this.seat = seatId;
        this.price = price;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seatId) {
        this.seat = seatId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
