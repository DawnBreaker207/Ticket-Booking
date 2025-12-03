package com.dawn.backend.repository;

import com.dawn.backend.model.Movie;
import com.dawn.backend.model.Showtime;
import com.dawn.backend.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    //    Find showtime by date
    List<Showtime> findByShowDate(LocalDate date);

    //    Get all showtime for a movie
    List<Showtime> findByMovie(Movie movie);

    //    Get all showtime at a theater
    @Query(value ="""
              SELECT s FROM Showtime s
              WHERE
                 (:#{#theater.getId()} IS NULL OR s.theater.id = :#{#theater.getId()})
              AND (
                  :#{#from} IS NULL
                  OR :#{#to} IS NULL
                  OR (s.showDate BETWEEN :#{#from} AND :#{#to})
              )
           """)
    Page<Showtime> findByTheater(Theater theater,LocalDate from, LocalDate to, Pageable pageable);

    //    Find by date and movie
    List<Showtime> findByShowDateAndMovie(LocalDate date, Movie movie);

    //    Find by date and theater
    List<Showtime> findByShowDateAndTheater(LocalDate date, Theater theater);

    //    Find available showtime from a date
    @Query("SELECT s FROM Showtime s WHERE s.showDate >= :date AND s.availableSeats > 0")
    List<Showtime> findAvailableShowtimeFromDate(LocalDate date);

    //    Find available showtime for a specific movie from date
    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.showDate >= :date AND s.availableSeats > 0")
    List<Showtime> findAvailableShowtimeForMovieFromDate(Long movieId, LocalDate date);

    @Query(value = """
            SELECT ROUND(AVG(utilization),2) AS seat_utilization
            FROM (
            SELECT
            	CASE
            		WHEN s.total_seats = 0 THEN 0
            		ELSE SUM(CASE WHEN r.id IS NOT NULL THEN 1 ELSE 0 END)
                        / s.total_seats * 100
            	END AS utilization
            FROM
            	showtime s
            LEFT JOIN seat se ON
            	se.showtime_id = s.id
            LEFT JOIN reservation r ON
            	r.id = se.reservation_id
                AND r.status = 'CONFIRMED'
            WHERE (:theaterId IS NULL OR s.theater_id = :theaterId)
                AND (:from IS NULL OR s.show_date >= :from)
                AND (:to IS NULL OR s.show_date <= :to)
            GROUP BY s.id
            ) AS t
            """, nativeQuery = true)
    Double getSeatUtilization(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("theaterId") Long theaterId);
}
