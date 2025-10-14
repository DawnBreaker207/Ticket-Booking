package com.example.backend.helper;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.model.Movie;

public interface MovieMappingHelper {
    static Movie map(final MovieRequestDTO m) {
        return Movie.builder()
                .title(m.getTitle())
                .poster(m.getPoster())
                .overview(m.getOverview())
                .duration(m.getDuration())
                .genres(m.getGenres())
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
                .genres(m.getGenres())
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
