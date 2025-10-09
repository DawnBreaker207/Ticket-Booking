package com.example.backend.service;

import com.example.backend.dto.request.*;
import com.example.backend.dto.response.ReservationResponseDTO;
import com.example.backend.model.Reservation;

import java.util.List;

public interface ReservationService {
    List<ReservationResponseDTO> findAll(ReservationFilterDTO o);

    Reservation findOne(String id);

    String initReservation(ReservationInitRequestDTO reservation);

    void holdSeats(ReservationHoldSeatRequestDTO reservation);

    ReservationResponseDTO confirm(ReservationRequestDTO reservation);

}
