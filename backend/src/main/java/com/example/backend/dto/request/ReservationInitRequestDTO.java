package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReservationInitRequestDTO {
    private String reservationId;

    private Long userId;

    private Long showtimeId;

    private Long theaterId;
}
