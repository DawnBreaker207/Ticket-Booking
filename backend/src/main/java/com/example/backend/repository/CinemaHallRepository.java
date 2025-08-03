package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import com.example.backend.config.SeatStatus;
import com.example.backend.model.CinemaHall;

public interface CinemaHallRepository extends DAO<CinemaHall> {
    Optional<CinemaHall> findByMovieIdAndMovieSession(Long movieId, String movieSession);
    void updateSeatStatus(List<String> seatIds, SeatStatus status);
    
}
