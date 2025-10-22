package com.example.backend.service;

import com.example.backend.dto.request.ReservationFilterDTO;
import com.example.backend.dto.request.ReservationHoldSeatRequestDTO;
import com.example.backend.dto.request.ReservationInitRequestDTO;
import com.example.backend.dto.request.ReservationRequestDTO;
import com.example.backend.dto.response.ReservationInitResponseDTO;
import com.example.backend.dto.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    List<ReservationResponseDTO> findAll(ReservationFilterDTO o);

    ReservationResponseDTO findOne(String id);

    ReservationInitResponseDTO initReservation(ReservationInitRequestDTO reservation);

    void holdReservationSeats(ReservationHoldSeatRequestDTO reservation);

    ReservationResponseDTO confirmReservation(ReservationRequestDTO reservation);

}
