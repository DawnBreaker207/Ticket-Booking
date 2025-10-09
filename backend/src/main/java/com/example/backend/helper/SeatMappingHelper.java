package com.example.backend.helper;

import com.example.backend.dto.request.SeatRequestDTO;
import com.example.backend.dto.response.SeatResponseDTO;
import com.example.backend.model.Reservation;
import com.example.backend.model.Seat;

public interface SeatMappingHelper {
    static Seat map(final SeatRequestDTO seat) {
        return Seat.builder()
                .id(seat.getId())
                .showtime(seat.getShowtime())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .reservation(seat.getReservation())
                .build();
    }

    static SeatResponseDTO map(final Seat seat) {
        return SeatResponseDTO.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .showtimeId(seat.getShowtime().getId())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }
}
