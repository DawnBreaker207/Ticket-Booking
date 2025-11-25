package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.MovieRequestDTO;
import com.dawn.backend.dto.response.MovieResponseDTO;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    ResponsePage<MovieResponseDTO> findAll(MovieRequestDTO m, Pageable pageable);

    MovieResponseDTO findOne(Long id);

    MovieResponseDTO findByMovieId(String id);

    MovieResponseDTO create(MovieRequestDTO id);

    MovieResponseDTO update(Long id, MovieRequestDTO m);

    void delete(Long id);
}
