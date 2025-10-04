package com.example.backend.dto.request;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Movie;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CinemaHallRequestDTO extends AbstractMappedEntity {
    public Long id;
    public Date movieSession;
    public Movie movie;
    private List<String> seatCodes = new ArrayList<>();
    private SeatStatus seatStatus;
}
