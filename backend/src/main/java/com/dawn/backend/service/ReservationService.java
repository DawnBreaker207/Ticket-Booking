package com.dawn.backend.service;

import com.dawn.backend.constant.ReservationStatus;
import com.dawn.backend.dto.request.ReservationFilterDTO;
import com.dawn.backend.dto.request.ReservationHoldSeatRequestDTO;
import com.dawn.backend.dto.request.ReservationInitRequestDTO;
import com.dawn.backend.dto.request.ReservationRequestDTO;
import com.dawn.backend.dto.response.ReservationInitResponseDTO;
import com.dawn.backend.dto.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    List<ReservationResponseDTO> findAll(ReservationFilterDTO o);

    ReservationResponseDTO findOne(String id);

    List<ReservationResponseDTO> findByUser(Boolean isPaid, ReservationStatus status);

    ReservationInitResponseDTO initReservation(ReservationInitRequestDTO reservation);

    void holdReservationSeats(ReservationHoldSeatRequestDTO reservation);

    ReservationResponseDTO confirmReservation(ReservationRequestDTO reservation);

    void cancelReservation(String reservationId, Long userId);

}
