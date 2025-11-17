package com.dawn.backend.service;

import com.dawn.backend.dto.response.SeatResponseDTO;
import com.dawn.backend.model.Seat;
import com.dawn.backend.model.Showtime;

import java.util.List;

public interface SeatService {

    List<SeatResponseDTO> getByShowtime(Long showtimeId);

    List<SeatResponseDTO> getAvailableSeatByShowtime(Long showtimeId);

    List<Seat> create(Showtime showtime);
}
