package com.example.backend.repository;

import com.example.backend.model.Movie;
import com.example.backend.model.Showtime;
import com.example.backend.model.Theater;
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
}
