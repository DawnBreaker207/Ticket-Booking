package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Hidden
@Entity
@Table(name = "order_seat")
public class OrderSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id",nullable = false)
    @JsonIgnoreProperties("cinemaHall")
    private Seat seat;

    @Column(name = "price")
    private BigDecimal price;

    public OrderSeat() {
        super();
    }

    public OrderSeat(OrderSeat order) {
        super();
        this.id = order.id;
        this.order = order.order;
        this.seat = order.seat;
        this.price = order.price;
    }

    public OrderSeat(
            Long id,
            Order order,
            Seat seat,
            BigDecimal price) {
        super();
        this.id = id;
        this.order = order;
        this.seat = seat;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
                ", order=" + order + '\'' +
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
                && Objects.equals(order, orderSeat.order)
                && Objects.equals(seat, orderSeat.seat)
                && Objects.equals(price, orderSeat.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, seat, price);
    }
}
