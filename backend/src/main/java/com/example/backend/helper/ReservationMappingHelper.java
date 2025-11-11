package com.example.backend.helper;

import com.example.backend.dto.response.ReservationResponseDTO;
import com.example.backend.model.Reservation;

public interface ReservationMappingHelper {
    static ReservationResponseDTO map(final Reservation reservation) {
        return
                ReservationResponseDTO
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
