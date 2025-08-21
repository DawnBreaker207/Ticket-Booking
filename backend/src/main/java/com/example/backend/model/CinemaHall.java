package com.example.backend.model;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;
import java.util.Objects;

@Hidden
public class CinemaHall extends AbstractMappedEntity {
    private Long id;
    private Long movieId;
    private String movieSession;

    private List<Seat> seats;

    public CinemaHall() {
        super();
    }

    public CinemaHall(Long id, Long movieId, String movieSession, List<Seat> seats) {
        super();
        this.id = id;
        this.movieId = movieId;
        this.movieSession = movieSession;
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieSession() {
        return movieSession;
    }

    public void setMovieSession(String movieSession) {
        this.movieSession = movieSession;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {

        return "CinemaHall(" +
                "id=" + id +
                ", movieId=" + movieId +
                ", movieSession='" + movieSession + '\'' +
                ", seats='" + seats + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CinemaHall cinemaHall = (CinemaHall) obj;
        return Objects.equals(id, cinemaHall.id)
                && Objects.equals(movieId, cinemaHall.movieId)
                && Objects.equals(movieSession, cinemaHall.movieSession)
                && Objects.equals(seats, cinemaHall.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieId, movieSession, seats);
    }

}
