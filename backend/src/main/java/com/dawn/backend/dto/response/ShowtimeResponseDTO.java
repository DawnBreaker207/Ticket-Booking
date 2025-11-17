package com.dawn.backend.dto.response;

import com.dawn.backend.model.AbstractMappedEntity;
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
public class ShowtimeResponseDTO extends AbstractMappedEntity {

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
