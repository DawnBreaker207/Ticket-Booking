package com.dawn.cinema.repository;

import com.dawn.catalog.model.Movie;
import com.dawn.cinema.model.Showtime;
import com.dawn.cinema.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    Page<Showtime> findByTheater(Theater theater, LocalDate from, LocalDate to, Pageable pageable);

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
}
