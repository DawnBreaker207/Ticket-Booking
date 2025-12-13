package com.dawn.api.cinema.dto;

import com.dawn.common.constant.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SeatDTO {
    private Long id;

    private String seatNumber;

    private String reservationId;

    private Long showtimeId;

    private SeatStatus status;
}
