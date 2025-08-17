package com.example.backend.dto;

import java.math.BigDecimal;

public class OrderSeatDTO {
    private Long seatId;
    private BigDecimal price;

    public OrderSeatDTO() {

    }

    public OrderSeatDTO(Long seatId, BigDecimal price) {
        this.seatId = seatId;
        this.price = price;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
