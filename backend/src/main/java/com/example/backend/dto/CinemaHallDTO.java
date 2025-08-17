package com.example.backend.dto;

import com.example.backend.constant.SeatStatus;

import java.util.List;

public class CinemaHallDTO {
    public String movieSession;
    public String orderTime;
    private List<String> seatCodes;
    private SeatStatus seatStatus;

    public CinemaHallDTO() {

    }

    public CinemaHallDTO(String movieSession, String orderTime, List<String> seatCodes, SeatStatus seatStatus) {
        this.movieSession = movieSession;
        this.orderTime = orderTime;
        this.seatCodes = seatCodes;
        this.seatStatus = seatStatus;
    }

    public String getMovieSession() {
        return movieSession;

    }

    public void setMovieSession(String movieSession) {
        this.movieSession = movieSession;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public List<String> getSeatCodes() {
        return seatCodes;
    }

    public void setSeatCodes(List<String> seatCodes) {
        this.seatCodes = seatCodes;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }
}
