package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.MovieDTO;
import com.example.backend.model.Movie;

public interface MovieService {


    List<Movie> findAll(MovieDTO m);

    Movie findOne(Long id);

    Movie findByMovieId(String id);

    Movie create(MovieDTO id);

    Movie createWithId(Long id);

    Movie update(Long id, Movie m);

    void delete(Long id);
}
