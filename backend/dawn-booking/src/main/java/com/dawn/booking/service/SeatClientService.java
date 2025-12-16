package com.dawn.booking.service;

import com.dawn.booking.dto.response.SeatDTO;

import java.util.List;

public interface SeatClientService {

    List<SeatDTO> findByIdWithLock(List<Long> seatIds);

    List<SeatDTO> findAllById(List<Long> seatIds);

    List<SeatDTO> findAllByReservationId(String reservationId);

    void saveAllSeat(List<SeatDTO> seats);
}
