package com.dawn.booking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReservationHoldSeatRequest {
    @NotNull
    private String reservationId;

    @NotNull
    private Long userId;

    @NotNull
    private Long showtimeId;

    @NotNull
    @NotEmpty
    private List<Long> seatIds;
}
