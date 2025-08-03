package com.example.backend.model;

import java.util.Objects;

import com.example.backend.config.SeatStatus;

public class Seat {
    private int id;
    private int cinemaHallId;
    private String seatNumber;
    private SeatStatus status;

    public Seat() {

    }

    public Seat(int id, int cinemaHallId, String seatNumber, SeatStatus status) {
	this.id = id;
	this.cinemaHallId = cinemaHallId;
	this.seatNumber = seatNumber;
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
        	", status=" + status + '\'' + 
        	"}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Seat seat = (Seat) obj;
        return Objects.equals(id, seat.id) 
        	&& Objects.equals(cinemaHallId, seat.cinemaHallId) 
        	&& Objects.equals(seatNumber, seat.seatNumber) 
        	&& Objects.equals(status, seat.status);
    }
    
    @Override
    public int hashCode() {
    return Objects.hash(id, cinemaHallId, seatNumber, status); 
    }
}
