package com.example.backend.helper;

import com.example.backend.dto.shared.CinemaHallDTO;
import com.example.backend.model.CinemaHall;

public interface CinemaHallMappingHelper {
    static CinemaHall map(CinemaHallDTO dto) {
        CinemaHall ch = new CinemaHall();
        ch.setId(dto.getId());
        ch.setMovieSession(dto.getMovieSession());
        ch.setMovie(dto.getMovie());
        return ch;
    }

    static CinemaHallDTO map(CinemaHall dto) {
        CinemaHallDTO ch = new CinemaHallDTO();
        ch.setId(dto.getId());
        ch.setMovieSession(dto.getMovieSession());
        ch.setMovie(dto.getMovie());
        return ch;
    }
}
