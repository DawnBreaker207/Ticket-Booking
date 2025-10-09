package com.example.backend.service;

import com.example.backend.dto.request.SeatRequestDTO;
import com.example.backend.dto.response.SeatResponseDTO;
import com.example.backend.model.Seat;
import com.example.backend.model.Showtime;

import java.util.List;

public interface SeatService {

    List<SeatResponseDTO> getByShowtime(Long showtimeId);

    List<SeatResponseDTO> getAvailableSeatByShowtime(Long showtimeId);

    List<Seat> create(Showtime showtime);
}
