package com.example.backend.repository;

import com.example.backend.dto.shared.MovieDTO;
import com.example.backend.model.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Mapper
@Repository
public interface MovieRepository extends DAO<Movie, Long> {
    List<Movie> findAllWithFilter(MovieDTO movie);

    @Override
    List<Movie> findAll();

    @Override
    Optional<Movie> findById(Long id);

    Optional<Movie> findByMovieId(String filmId);

    @Override
    int insert(Movie movie);

    default Movie save(Movie input) {
        if (input.getId() == null) {
            insert(input);
        } else {
            update(input);
        }
        return input;
    }
    @Override
    int update(Movie movie);

    @Override
    void delete(Long id);
}
