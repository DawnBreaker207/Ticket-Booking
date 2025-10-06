package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;
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
public class MovieResponseDTO extends AbstractMappedEntity {
    private Long id;

    private String title;

    private String poster;

    private String overview;

    private Integer duration;

    private List<String> genres = new ArrayList<>();

    private Date releaseDate;

    private String language;

    private String imdbId;

    private String filmId;
}
