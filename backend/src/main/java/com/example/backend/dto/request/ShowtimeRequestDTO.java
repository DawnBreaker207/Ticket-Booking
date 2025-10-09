package com.example.backend.dto.request;

import com.example.backend.model.*;
import jakarta.persistence.*;
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
public class ShowtimeRequestDTO  {

    private Long movieId;

    private Long theaterId;

    private LocalDate showDate;

    private LocalTime showTime;

    private BigDecimal price;

    private Integer totalSeats;
}
