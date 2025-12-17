package com.dawn.cinema.helper;

import com.dawn.cinema.dto.request.SeatRequest;
import com.dawn.cinema.dto.response.SeatResponse;
import com.dawn.cinema.model.Seat;
import com.dawn.cinema.model.Showtime;

public interface SeatMappingHelper {
    static Seat map(final SeatRequest seat, final Showtime showtime) {
        return Seat.builder()
                .id(seat.getId())
                .showtime(showtime)
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
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
