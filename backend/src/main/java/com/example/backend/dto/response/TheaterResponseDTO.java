package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Showtime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TheaterResponseDTO extends AbstractMappedEntity {
    private Long id;

    private String name;

    private String location;

    private Integer capacity;

    private Boolean isDeleted;

    private List<Showtime> showtime = new ArrayList<>();
}
