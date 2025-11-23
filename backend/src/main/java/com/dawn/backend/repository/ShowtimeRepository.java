package com.dawn.backend.repository;

import com.dawn.backend.model.Movie;
import com.dawn.backend.model.Showtime;
import com.dawn.backend.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    //    Find showtime by date
    List<Showtime> findByShowDate(LocalDate date);

    //    Get all showtime for a movie
    List<Showtime> findByMovie(Movie movie);

    //    Get all showtime at a theater
    List<Showtime> findByTheater(Theater theater);

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
            SELECT
            	CASE
            		WHEN SUM(s.total_seats) = 0 THEN 0
            		ELSE ROUND(
                        SUM(CASE WHEN r.status = 'CONFIRMED' IS NOT NULL THEN 1 ELSE 0 END) / SUM(s.total_seats) * 100, 2)
            	END AS seat_utilization
            FROM
            	showtime s
            LEFT JOIN seat se ON
            	se.showtime_id = s.id
            LEFT JOIN reservation r ON
            	r.id = se.reservation_id
            AND r.status = 'CONFIRMED'
            WHERE
                (:from IS NULL OR r.created_at >= :from)
                AND
                (:to IS NULL OR r.created_at <= :to)
                AND
                (:theaterId IS NULL OR s.theater_id = :theaterId)
            """, nativeQuery = true)
    Double getSeatUtilization(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("theaterId") Long theaterId);
}
