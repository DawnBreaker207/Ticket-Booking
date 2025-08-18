package com.example.backend.model;

import io.swagger.v3.oas.annotations.Hidden;

import java.math.BigDecimal;
import java.util.Objects;

@Hidden
public class OrderSeat {
    private Long id;
    private String orderId;
    private Long seatId;
    private BigDecimal price;

    public OrderSeat() {

    }

    public OrderSeat(
            Long id,
            String orderId,
            Long seatId,
            BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.seatId = seatId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", orderId=" + orderId + '\'' +
                ", seatId=" + seatId + '\'' +
                ", price=" + price + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderSeat orderSeat = (OrderSeat) obj;
        return Objects.equals(id, orderSeat.id)
                && Objects.equals(orderId, orderSeat.orderId)
                && Objects.equals(seatId, orderSeat.seatId)
                && Objects.equals(price, orderSeat.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, seatId, price);
    }
}
