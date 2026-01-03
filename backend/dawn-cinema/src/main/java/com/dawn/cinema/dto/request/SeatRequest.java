package com.dawn.cinema.dto.request;

import com.dawn.cinema.model.Showtime;
import com.dawn.common.constant.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SeatRequest {

    private Long id;

    private String seatNumber;

    private Long showtimeId;

    private String reservationId;

    private SeatStatus status;
}
