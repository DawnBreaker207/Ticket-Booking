package com.example.backend.service;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.model.Movie;

import java.util.List;

public interface MovieService {
    List<MovieResponseDTO> findAll(MovieRequestDTO m);

    Movie findOne(Long id);

    Movie findByMovieId(String id);

    Movie create(MovieRequestDTO id);

    Movie createWithId(Long id);

    Movie update(Long id, Movie m);

    void delete(Long id);
}
