package com.dawn.cinema.service;

import com.dawn.cinema.dto.response.SeatResponse;
import com.dawn.cinema.model.Seat;
import com.dawn.cinema.model.Showtime;

import java.util.List;

public interface SeatService {

    List<SeatResponse> getByShowtime(Long showtimeId);

    List<SeatResponse> getAvailableSeatByShowtime(Long showtimeId);

    List<Seat> create(Showtime showtime);
}
