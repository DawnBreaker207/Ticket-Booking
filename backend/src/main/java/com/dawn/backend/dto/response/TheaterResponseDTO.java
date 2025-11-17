package com.dawn.backend.dto.response;

import com.dawn.backend.model.AbstractMappedEntity;
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

    private List<ShowtimeResponseDTO> showtime = new ArrayList<>();
}
