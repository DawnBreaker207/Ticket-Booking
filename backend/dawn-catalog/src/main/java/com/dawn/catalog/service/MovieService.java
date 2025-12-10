package com.dawn.catalog.service;

import com.dawn.catalog.dto.request.MovieRequest;
import com.dawn.catalog.dto.response.MovieResponse;
import com.dawn.common.config.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    ResponsePage<MovieResponse> findAll(MovieRequest m, Pageable pageable);

    MovieResponse findOne(Long id);

    MovieResponse findByMovieId(String id);

    MovieResponse create(MovieRequest id);

    MovieResponse update(Long id, MovieRequest m);

    void delete(Long id);
}
