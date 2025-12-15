package com.dawn.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReservationInitResponse {

    private String reservationId;

    private Long showtimeId;

    private Long ttl;

    private Instant expiredAt;
}
