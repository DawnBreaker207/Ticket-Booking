package com.example.backend.repository;

import java.util.Optional;

import com.example.backend.model.Movie;

public interface MovieRepository extends DAO<Movie> {
    Optional<Movie> findByMovieId(String id);
}
