package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.*;
import com.dawn.backend.dto.response.ReservationInitResponseDTO;
import com.dawn.backend.dto.response.ReservationResponseDTO;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
    ResponsePage<ReservationResponseDTO> findAll(ReservationFilterDTO o, Pageable pageable);

    ResponsePage<ReservationResponseDTO> findByUser(ReservationUserRequestDTO request, Pageable pageable);

    ReservationResponseDTO findOne(String id);

    ReservationInitResponseDTO initReservation(ReservationInitRequestDTO reservation);

    void holdReservationSeats(ReservationHoldSeatRequestDTO reservation);

    ReservationResponseDTO confirmReservation(ReservationRequestDTO reservation);

    void cancelReservation(String reservationId, Long userId);

}
