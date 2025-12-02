package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.*;
import com.dawn.backend.dto.response.ReservationInitResponse;
import com.dawn.backend.dto.response.ReservationResponse;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
    ResponsePage<ReservationResponse> findAll(ReservationFilterRequest request, Pageable pageable);

    ResponsePage<ReservationResponse> findByUser(ReservationUserRequest request, Pageable pageable);

    ReservationResponse findOne(String id);

    ReservationInitResponse initReservation(ReservationInitRequest reservation);

    void holdReservationSeats(ReservationHoldSeatRequest reservation);

    ReservationResponse confirmReservation(ReservationRequest reservation);

    void cancelReservation(String reservationId, Long userId);

}
