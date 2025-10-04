package com.example.backend.helper;

import com.example.backend.dto.request.CinemaHallRequestDTO;
import com.example.backend.dto.response.CinemaHallResponseDTO;
import com.example.backend.model.CinemaHall;
import com.example.backend.model.Seat;

import java.util.ArrayList;
import java.util.List;

public interface CinemaHallMappingHelper {
    static CinemaHall map(CinemaHallRequestDTO dto) {
        CinemaHall ch = new CinemaHall();

        if (dto.getId() != null) {
            ch.setId(dto.getId());
        }

        if (dto.getMovieSession() != null) {
            ch.setMovieSession(dto.getMovieSession());
        }

        if (dto.getMovie() != null && dto.getMovie().getId() != null) {
            ch.setMovie(dto.getMovie());
        }

        if (dto.getSeatCodes() != null && !dto.getSeatCodes().isEmpty()) {
            List<Seat> seats = new ArrayList<>();
            for (String code : dto.getSeatCodes()) {
                Seat seat = new Seat();
                seat.setCinemaHall(ch);
                seat.setSeatNumber(code);
                seats.add(seat);
            }
            ch.setSeats(seats);
        }
        return ch;
    }

    static CinemaHallResponseDTO map(CinemaHall dto) {
        CinemaHallResponseDTO ch = new CinemaHallResponseDTO();

        if (dto.getId() != null) {
            ch.setId(dto.getId());
        }

        if (dto.getMovieSession() != null) {
            ch.setMovieSession(dto.getMovieSession());
        }

        if (dto.getMovie() != null && dto.getMovie().getId() != null) {
            ch.setMovie(dto.getMovie());
        }

        ch.setSeatCodes(dto.getSeats().stream().map(Seat::getSeatNumber).toList());
        return ch;
    }
}
