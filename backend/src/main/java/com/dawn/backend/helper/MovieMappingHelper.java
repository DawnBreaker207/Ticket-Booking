package com.dawn.backend.helper;

import com.dawn.backend.dto.request.MovieRequestDTO;
import com.dawn.backend.dto.response.MovieResponseDTO;
import com.dawn.backend.model.Genre;
import com.dawn.backend.model.Movie;

import java.util.stream.Collectors;

public interface MovieMappingHelper {
    static Movie map(final MovieRequestDTO m) {
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

    static MovieResponseDTO map(final Movie m) {
        return MovieResponseDTO.builder()
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
}
