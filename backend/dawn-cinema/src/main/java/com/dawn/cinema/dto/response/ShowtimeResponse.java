package com.dawn.cinema.dto.response;

import com.dawn.common.core.dto.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowtimeResponse extends BaseResponse {

    private Long id;
    private Long movieId;
    private String movieTitle;
    private String moviePosterUrl;

    private Long theaterId;
    private String theaterName;
    private String theaterLocation;

    private LocalDate showDate;
    private LocalTime showTime;

    private BigDecimal price;

    private Integer totalSeats;
    private Integer availableSeats;

}
