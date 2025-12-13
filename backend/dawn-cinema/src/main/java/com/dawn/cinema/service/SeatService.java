package com.dawn.cinema.service;

import com.dawn.api.cinema.service.SeatClientService;
import com.dawn.cinema.dto.response.SeatResponse;
import com.dawn.cinema.model.Seat;
import com.dawn.cinema.model.Showtime;

import java.util.List;

public interface SeatService extends SeatClientService {

    List<SeatResponse> getByShowtime(Long showtimeId);

    List<SeatResponse> getAvailableSeatByShowtime(Long showtimeId);

    List<Seat> create(Showtime showtime);
}
