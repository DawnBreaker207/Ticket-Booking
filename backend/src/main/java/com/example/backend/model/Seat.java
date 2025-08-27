package com.example.backend.model;

import com.example.backend.constant.SeatStatus;
import io.swagger.v3.oas.annotations.Hidden;

import java.math.BigDecimal;
import java.util.Objects;

@Hidden
public class Seat {
    private int id;
    private int cinemaHallId;
    private String seatNumber;
    private BigDecimal price;
    private SeatStatus status;

    public Seat() {
        super();
    }

    public Seat(Seat seat) {
        super();
        this.id = seat.id;
        this.cinemaHallId = seat.cinemaHallId;
        this.seatNumber = seat.seatNumber;
        this.price = seat.price;
        this.status = seat.status;

    }

    public Seat(int id, int cinemaHallId, String seatNumber, BigDecimal price, SeatStatus status) {
        super();
        this.id = id;
        this.cinemaHallId = cinemaHallId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCinemaHallId() {
        return cinemaHallId;
    }

    public void setCinemaHallId(int cinemaHallId) {
        this.cinemaHallId = cinemaHallId;
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

    public SeatStatus getSeatStatus() {
        return status;
    }

    public void setSeatStatus(SeatStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", cinemaHallId=" + cinemaHallId + '\'' +
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
                && Objects.equals(cinemaHallId, seat.cinemaHallId)
                && Objects.equals(seatNumber, seat.seatNumber)
                && Objects.equals(price, seat.price)
                && Objects.equals(status, seat.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cinemaHallId, seatNumber, price, status);
    }
}
