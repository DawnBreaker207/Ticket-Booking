package com.example.backend.service;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.model.Movie;

import java.util.List;

public interface MovieService {
    List<MovieResponseDTO> findAll(MovieRequestDTO m);

    MovieResponseDTO findOne(Long id);

    MovieResponseDTO findByMovieId(String id);

    MovieResponseDTO create(MovieRequestDTO id);

    MovieResponseDTO createWithId(Long id);

    MovieResponseDTO update(Long id, Movie m);

    void delete(Long id);
}
