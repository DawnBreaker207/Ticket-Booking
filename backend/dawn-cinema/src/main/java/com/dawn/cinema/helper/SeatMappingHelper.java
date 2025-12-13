package com.dawn.cinema.helper;

import com.dawn.api.cinema.dto.SeatDTO;
import com.dawn.cinema.dto.request.SeatRequest;
import com.dawn.cinema.dto.response.SeatResponse;
import com.dawn.cinema.model.Seat;
import com.dawn.cinema.model.Showtime;

public interface SeatMappingHelper {
    static Seat map(final SeatRequest seat) {
        return Seat.builder()
                .id(seat.getId())
                .showtime(seat.getShowtime())
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

    static SeatDTO mapDto(final Seat seat) {
        return SeatDTO
                .builder()
                .id(seat.getId())
                .showtimeId(seat.getShowtime().getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build();
    }

    static Seat map(final SeatDTO seat) {
        return Seat
                .builder()
                .id(seat.getId())
                .showtime(Showtime.builder()
                        .id(seat.getShowtimeId())
                        .build())
                .reservationId(seat.getReservationId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build();
    }
}
