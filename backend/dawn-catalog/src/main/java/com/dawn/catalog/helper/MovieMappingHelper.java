package com.dawn.catalog.helper;


import com.dawn.api.catalog.dto.MovieDTO;
import com.dawn.catalog.dto.request.MovieRequest;
import com.dawn.catalog.dto.response.MovieResponse;
import com.dawn.catalog.model.Genre;
import com.dawn.catalog.model.Movie;

import java.util.stream.Collectors;

public interface MovieMappingHelper {
    static Movie map(final MovieRequest m) {
        return Movie.builder()
                .title(m.getTitle())
                .poster(m.getPoster())
                .overview(m.getOverview())
                .duration(m.getDuration())
                .releaseDate(m.getReleaseDate())
                .imdbId(m.getImdbId())
                .filmId(m.getFilmId())
                .language(m.getLanguage())
                .build();
    }

    static MovieResponse map(final Movie m) {
        return MovieResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .poster(m.getPoster())
                .overview(m.getOverview())
                .duration(m.getDuration())
                .genres(m
                        .getGenres()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.toSet()))
                .releaseDate(m.getReleaseDate())
                .imdbId(m.getImdbId())
                .filmId(m.getFilmId())
                .language(m.getLanguage())
                .isDeleted(m.getIsDeleted())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }

    static MovieDTO mapDto(final Movie m) {
        return MovieDTO.builder()
                .id(m.getId())
                .title(m.getTitle())
                .poster(m.getPoster())
                .build();
    }
}
