package com.example.backend.service;

import com.example.backend.dto.shared.MovieDTO;
import com.example.backend.model.Movie;

import java.util.List;

public interface MovieService {


    List<Movie> findAll(MovieDTO m);

    Movie findOne(Long id);

    Movie findByMovieId(String id);

    Movie create(MovieDTO id);

    Movie createWithId(Long id);

    Movie update(Long id, Movie m);

    void delete(Long id);
}
