package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.MovieRequest;
import com.dawn.backend.dto.response.MovieResponse;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    ResponsePage<MovieResponse> findAll(MovieRequest m, Pageable pageable);

    MovieResponse findOne(Long id);

    MovieResponse findByMovieId(String id);

    MovieResponse create(MovieRequest id);

    MovieResponse update(Long id, MovieRequest m);

    void delete(Long id);
}
