package com.dawn.backend.dto.response;

import com.dawn.backend.model.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MovieResponse extends AbstractMappedEntity {
    private Long id;

    private String title;

    private String poster;

    private String overview;

    private Integer duration;

    private Set<String> genres = new HashSet<>();

    private Date releaseDate;

    private String language;

    private String imdbId;

    private String filmId;

    private Boolean isDeleted;
}
