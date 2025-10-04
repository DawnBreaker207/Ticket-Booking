package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Hidden
@Entity
@Table(name = "order_seat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    @JsonIgnoreProperties("cinemaHall")
    private Seat seat;

    @Column(name = "price")
    private BigDecimal price;

}
