package com.dawn.backend.helper;

import com.dawn.backend.dto.response.ReservationResponse;
import com.dawn.backend.model.Reservation;

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

}
