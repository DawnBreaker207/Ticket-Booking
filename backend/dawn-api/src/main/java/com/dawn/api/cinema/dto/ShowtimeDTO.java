package com.dawn.api.cinema.dto;

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
public class ShowtimeDTO {
    private Long id;

    private BigDecimal price;

    private Long movieId;

    private String theaterName;

    private LocalDate showDate;

    private LocalTime showTime;

    private Integer availableSeats;
}
