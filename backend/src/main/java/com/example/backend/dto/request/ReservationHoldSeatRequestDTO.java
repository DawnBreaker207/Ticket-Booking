package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReservationHoldSeatRequestDTO {
    private String reservationId;

    private Long userId;

    private Long showtimeId;

    private List<Long> seatIds;
}
