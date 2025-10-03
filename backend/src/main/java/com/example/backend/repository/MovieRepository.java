package com.example.backend.repository;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.model.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Mapper
@Repository
public interface MovieRepository extends DAO<Movie, Long> {
    List<Movie> findAllWithFilter(MovieRequestDTO movie);

    @Override
    List<Movie> findAll();

    @Override
    Optional<Movie> findById(Long id);

    Optional<Movie> findByMovieId(String filmId);


    int insert(MovieRequestDTO movie);

    default Movie save(Movie input) {
        if (input.getId() == null) {
            insert(input);
        } else {
            update(input);
        }
        return input;
    }
    
    int update(MovieRequestDTO movie);

    @Override
    void delete(Long id);
}
