package com.example.backend.dto.response;

import com.example.backend.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
