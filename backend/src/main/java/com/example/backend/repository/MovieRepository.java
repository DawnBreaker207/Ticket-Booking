package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import com.example.backend.dto.MovieDTO;
import com.example.backend.model.Movie;

public interface MovieRepository extends DAO<Movie> {
    List<Movie> findAll(MovieDTO movie);

    Optional<Movie> findByMovieId(String id);
}
