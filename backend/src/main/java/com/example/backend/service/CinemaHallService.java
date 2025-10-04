package com.example.backend.service;

import java.util.Date;
import java.util.List;

import com.example.backend.constant.SeatStatus;
import com.example.backend.dto.response.CinemaHallResponseDTO;
import com.example.backend.model.CinemaHall;

public interface CinemaHallService {

    List<CinemaHallResponseDTO> findAll();

    CinemaHall findOne(Long id);

    CinemaHall findByMovieAndSession(Long movieId, Date movieSession);

    CinemaHall createMovieSchedule(CinemaHall cinemaHall);

    CinemaHall updateMovieSchedule(Long id, CinemaHall cinemaHall);

    void removeMovieSchedule(Long id);

}
