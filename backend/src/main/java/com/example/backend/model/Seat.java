package com.example.backend.model;

import com.example.backend.constant.SeatStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Hidden
@Entity
@Table(name = "seat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_hall_id", nullable = false)
    @JsonIgnore
    private CinemaHall cinemaHall;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;


}
