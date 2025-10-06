package com.example.backend.repository;

import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.model.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = """
            SELECT *
            FROM movie AS m
            WHERE 
                (:#{#movie.getTitle()} IS NULL OR m.title LIKE CONCAT ('%',:#{#movie.getTitle()},'%'))
                AND (:#{#movie.getDuration()} IS NULL OR m.duration  = :#{#movie.getDuration()})
                AND (:#{#movie.getReleaseDate()} IS NULL OR m.release_date = :#{#movie.getReleaseDate()})
            ORDER BY id DESC
            """, nativeQuery = true)
    List<Movie> findAllWithFilter(MovieRequestDTO movie);

    Optional<Movie> findByFilmId(String filmId);

}
