package com.example.backend.dto.response;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CinemaHallResponseDTO extends AbstractMappedEntity {
    public Long id;
    public Date movieSession;
    public Movie movie;
    private List<String> seatCodes = new ArrayList<>();
    private SeatStatus seatStatus;


}
