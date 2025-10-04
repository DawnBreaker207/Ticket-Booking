package com.example.backend.model;

import com.example.backend.constant.SeatStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Hidden
@Entity
@Table(name = "seat")
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

    public Seat() {
        super();
    }

    public Seat(Seat seat) {
        super();
        this.id = seat.id;
        this.cinemaHall = seat.cinemaHall;
        this.seatNumber = seat.seatNumber;
        this.price = seat.price;
        this.status = seat.status;

    }

    public Seat(Long id, CinemaHall cinemaHall, String seatNumber, BigDecimal price, SeatStatus status) {
        super();
        this.id = id;
        this.cinemaHall = cinemaHall;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public void setCinemaHall(CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", cinemaHall=" + cinemaHall + '\'' +
                ", seatNumber=" + seatNumber + '\'' +
                ", price=" + price + '\'' +
                ", status=" + status + '\'' + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Seat seat = (Seat) obj;
        return Objects.equals(id, seat.id)
                && Objects.equals(cinemaHall, seat.cinemaHall)
                && Objects.equals(seatNumber, seat.seatNumber)
                && Objects.equals(price, seat.price)
                && Objects.equals(status, seat.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cinemaHall, seatNumber, price, status);
    }
}
