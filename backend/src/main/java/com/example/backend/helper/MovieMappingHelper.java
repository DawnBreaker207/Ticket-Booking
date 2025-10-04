package com.example.backend.helper;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.model.Movie;

public interface MovieMappingHelper {
    static Movie map(final MovieRequestDTO m) {
        return new Movie(
                m.getTitle(),
                m.getPoster(),
                m.getOverview(),
                m.getDuration(),
                m.getGenres(),
                m.getReleaseDate(),
                m.getImdbId(),
                m.getFilmId()
        );
    }

    static MovieResponseDTO map(final Movie m) {
        return new MovieResponseDTO(
                m.getId(),
                m.getTitle(),
                m.getPoster(),
                m.getOverview(),
                m.getDuration(),
                m.getGenres(),
                m.getReleaseDate(),
                m.getImdbId(),
                m.getFilmId()
        );
    }
}
