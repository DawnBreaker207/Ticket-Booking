package com.example.backend.dto.request;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Movie;
import com.example.backend.model.Showtime;
import jakarta.persistence.*;
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
public class TheaterRequestDTO  {

    private String name;

    private String location;

    private Integer capacity;

}
