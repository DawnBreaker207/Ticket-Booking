package com.example.backend.model;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Objects;

@Hidden
@Alias("OrderSeat")
public class OrderSeat {
    private Long id;
    private String orderId;
    private Seat seat;
    private BigDecimal price;

    public OrderSeat() {
        super();
    }

    public OrderSeat(OrderSeat order) {
        super();
        this.id = order.id;
        this.orderId = order.orderId;
        this.seat = order.seat;
        this.price = order.price;
    }

    public OrderSeat(
            Long id,
            String orderId,
            Seat seat,
            BigDecimal price) {
        super();
        this.id = id;
        this.orderId = orderId;
        this.seat = seat;
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

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
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
                ", seat=" + seat + '\'' +
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
                && Objects.equals(seat, orderSeat.seat)
                && Objects.equals(price, orderSeat.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, seat, price);
    }
}
