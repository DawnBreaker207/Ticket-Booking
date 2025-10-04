package com.example.backend.service;

import com.example.backend.dto.response.CinemaHallResponseDTO;
import com.example.backend.model.CinemaHall;

import java.util.Date;
import java.util.List;

public interface CinemaHallService {

    List<CinemaHallResponseDTO> findAll();

    CinemaHall findOne(Long id);

    CinemaHall findByMovieAndSession(Long movieId, Date movieSession);

    CinemaHall createMovieSchedule(CinemaHall cinemaHall);

    CinemaHall updateMovieSchedule(Long id, CinemaHall cinemaHall);

    void removeMovieSchedule(Long id);

}
