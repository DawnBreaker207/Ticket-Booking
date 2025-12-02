package com.dawn.backend.service;

import com.dawn.backend.dto.response.SeatResponse;
import com.dawn.backend.model.Seat;
import com.dawn.backend.model.Showtime;

import java.util.List;

public interface SeatService {

    List<SeatResponse> getByShowtime(Long showtimeId);

    List<SeatResponse> getAvailableSeatByShowtime(Long showtimeId);

    List<Seat> create(Showtime showtime);
}
