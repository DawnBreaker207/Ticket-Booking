package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReservationRequestDTO{

    private String reservationId;

    private Long showtimeId;

    private Long userId;

    private List<Long> seats = new ArrayList<>();
}
