package com.dawn.backend.helper;

import com.dawn.backend.dto.response.ReservationResponse;
import com.dawn.backend.dto.response.UserReservationResponse;
import com.dawn.backend.model.Reservation;
import com.dawn.backend.model.Seat;

public interface ReservationMappingHelper {

    static ReservationResponse map(final Reservation reservation) {
        return
                ReservationResponse
                        .builder()
                        .id(reservation.getId())
                        .user(UserMappingHelper.map(reservation.getUser()))
                        .showtime(ShowtimeMappingHelper.map(reservation.getShowtime()))
                        .reservationStatus(reservation.getReservationStatus())
                        .totalAmount(reservation.getTotalAmount())
                        .seats(reservation
                                .getSeats()
                                .stream()
                                .map(SeatMappingHelper::map)
                                .toList())
                        .isDeleted(reservation.getIsDeleted())
                        .isPaid(reservation.getIsPaid())
                        .createdAt(reservation.getCreatedAt())
                        .updatedAt(reservation.getUpdatedAt())
                        .build();

    }

    static UserReservationResponse toUserResponse(final Reservation reservation){
        return UserReservationResponse.builder()
                .reservationId(reservation.getId())
                .movieTitle(reservation.getShowtime().getMovie().getTitle())
                .moviePoster(reservation.getShowtime().getMovie().getPoster())
                .showtime(reservation.getShowtime().getShowTime().toString())
                .date(reservation.getShowtime().getShowDate())
                .time(reservation.getShowtime().getShowTime())
                .theater(reservation.getShowtime().getTheater().getName())
                .seats(reservation.getSeats().stream().map(Seat::getSeatNumber).toList())
                .amount(reservation.getTotalAmount().intValue())
                .build();
    }

}
