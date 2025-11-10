package com.example.backend.service;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;

import java.util.List;

public interface MovieService {
    List<MovieResponseDTO> findAll(MovieRequestDTO m);

    MovieResponseDTO findOne(Long id);

    MovieResponseDTO findByMovieId(String id);

    MovieResponseDTO create(MovieRequestDTO id);

    MovieResponseDTO update(Long id, MovieRequestDTO m);

    void delete(Long id);
}
