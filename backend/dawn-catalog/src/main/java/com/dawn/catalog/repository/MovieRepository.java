package com.dawn.backend.repository;

import com.dawn.backend.dto.request.MovieRequest;
import com.dawn.backend.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = """
            SELECT *
            FROM movie AS m
            WHERE
                (:#{#movie.title} IS NULL OR m.title LIKE CONCAT ('%' , :#{#movie.title}, '%'))
                AND (:#{#movie.duration} IS NULL OR m.duration  = :#{#movie.duration})
                AND (:#{#movie.releaseDate} IS NULL OR m.release_date = :#{#movie.releaseDate})
                AND ( m.is_deleted = false OR m.is_deleted IS NULL)
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Movie> findAllWithFilter(@Param("movie") MovieRequest movie, Pageable pageable);

    Optional<Movie> findByFilmId(String filmId);
}
