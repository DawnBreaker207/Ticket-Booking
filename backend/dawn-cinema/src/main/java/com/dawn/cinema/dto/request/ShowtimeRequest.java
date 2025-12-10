package com.dawn.cinema.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ShowtimeRequest {

    private Long movieId;

    private Long theaterId;

    private LocalDate showDate;

    private LocalTime showTime;

    private BigDecimal price;

    private Integer totalSeats;
}
