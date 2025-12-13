package com.dawn.api.cinema.service;

import com.dawn.api.cinema.dto.SeatDTO;

import java.util.List;

public interface SeatClientService {
    List<SeatDTO> findByIdWithLock(List<Long> seatIds);


    List<SeatDTO> findAllById(List<Long> seatIds);

    List<Long> findAllByReservationId(String reservationId);

    void saveAllSeat(List<SeatDTO> seats);
}
