package com.dawn.backend.repository;

import com.dawn.backend.dto.request.MovieRequestDTO;
import com.dawn.backend.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
    Page<Movie> findAllWithFilter(@Param("movie") MovieRequestDTO movie, Pageable pageable);

    Optional<Movie> findByFilmId(String filmId);


    @Query(value = """
            SELECT
            	m.title AS movieTitle,
            	COALESCE(COUNT(se.id), 0) AS ticketSold,
            	COALESCE(SUM(p.amount), 0) AS totalRevenue
            FROM
            	movie m
            JOIN showtime s ON
            	s.movie_id = m.id
            LEFT JOIN reservation r ON
            	r.showtime_id = s.id
            	AND r.is_deleted = false
            LEFT JOIN seat se ON
                se.reservation_id = r.id
            LEFT JOIN payment p ON
            	p.reservation_id = r.id
            WHERE
            	(:from IS NULL OR r.created_at >= :from)
                AND
                (:to IS NULL OR r.created_at <= :to)
            GROUP BY
            	m.id,
            	m.title
            ORDER BY
            	ticketSold DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> getTopMovie(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
