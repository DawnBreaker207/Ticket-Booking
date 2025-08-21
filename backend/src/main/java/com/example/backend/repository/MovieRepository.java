package com.example.backend.repository;

import com.example.backend.dto.shared.MovieDTO;
import com.example.backend.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends DAO<Movie> {
    List<Movie> findAll(MovieDTO movie);

    Optional<Movie> findByMovieId(String id);
}
