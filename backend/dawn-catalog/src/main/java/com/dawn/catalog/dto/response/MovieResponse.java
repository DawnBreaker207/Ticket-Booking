package com.dawn.catalog.dto.response;

import com.dawn.common.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MovieResponse extends BaseResponse {
    private Long id;

    private String title;

    private String poster;

    private String overview;

    private Integer duration;

    private Set<String> genres = new HashSet<>();

    private LocalDate releaseDate;

    private String language;

    private String imdbId;

    private String filmId;

    private Boolean isDeleted;
}
