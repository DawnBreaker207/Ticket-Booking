package com.dawn.backend.dto.request;

import com.dawn.backend.constant.SeatStatus;
import com.dawn.backend.model.Reservation;
import com.dawn.backend.model.Showtime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SeatRequestDTO {

    private Long id;

    private Showtime showtime;

    private String seatNumber;

    private SeatStatus status;

    private Reservation reservation;
}
