package com.dawn.backend.helper;

import com.dawn.backend.dto.request.SeatRequest;
import com.dawn.backend.dto.response.SeatResponse;
import com.dawn.backend.model.Seat;

public interface SeatMappingHelper {
    static Seat map(final SeatRequest seat) {
        return Seat.builder()
                .id(seat.getId())
                .showtime(seat.getShowtime())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .reservation(seat.getReservation())
                .build();
    }

    static SeatResponse map(final Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .showtimeId(seat.getShowtime().getId())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }
}
