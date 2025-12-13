package com.dawn.booking.helper;

import com.dawn.api.catalog.dto.MovieDTO;
import com.dawn.api.cinema.dto.ShowtimeDTO;
import com.dawn.booking.dto.response.ReservationResponse;
import com.dawn.booking.dto.response.UserReservationResponse;
import com.dawn.booking.model.Reservation;

import java.util.List;

public interface ReservationMappingHelper {

    static ReservationResponse map(final Reservation reservation) {
        return
                ReservationResponse
                        .builder()
                        .id(reservation.getId())
                        .userId(reservation.getUserId())
                        .showtimeId(reservation.getShowtimeId())
                        .reservationStatus(reservation.getReservationStatus())
                        .totalAmount(reservation.getTotalAmount())
//                        .seats(seats)
                        .isDeleted(reservation.getIsDeleted())
                        .isPaid(reservation.getIsPaid())
                        .createdAt(reservation.getCreatedAt())
                        .updatedAt(reservation.getUpdatedAt())
                        .build();

    }

    static UserReservationResponse toUserResponse(final Reservation reservation) {
        return UserReservationResponse.builder()
                .reservationId(reservation.getId())
//                .movieTitle(movie.getTitle())
//                .moviePoster(movie.getPoster())
//                .showtime(showtime.getId())
//                .date(showtime.getShowDate())
//                .time(showtime.getShowTime())
//                .theater(showtime.getTheaterName())
//                .seats(seats)
                .amount(reservation.getTotalAmount().intValue())
                .build();
    }

}
