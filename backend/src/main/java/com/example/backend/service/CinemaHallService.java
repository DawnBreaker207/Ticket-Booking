package com.example.backend.service;

import java.util.List;

import com.example.backend.config.SeatStatus;
import com.example.backend.model.CinemaHall;

public interface CinemaHallService {

    List<CinemaHall> findAll();

    CinemaHall findOne(Long id);

    CinemaHall findByMovieAndSession(Long movieId, String movieSession);

    CinemaHall createMovieSchedule(CinemaHall cinemaHall);

    CinemaHall updateMovieSchedule(Long id, CinemaHall cinemaHall);

    void removeMovieSchedule(Long id);

    void updateSeats(Long hallId, List<String> seatCodes, SeatStatus status);

}
