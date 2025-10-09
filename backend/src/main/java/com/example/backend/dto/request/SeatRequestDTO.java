package com.example.backend.dto.request;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Reservation;
import com.example.backend.model.Showtime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SeatRequestDTO {

    private Long id;

    private Showtime showtime;

    private String seatNumber;

    private SeatStatus status;

    private Reservation reservation;
}
