package com.example.backend.model;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Hidden
public class CinemaHall extends AbstractMappedEntity {
    private Long id;
    private Date movieSession;
    private List<Seat> seats;
    private Movie movie;

    public CinemaHall() {
        super();
    }


    public CinemaHall(CinemaHall cinemaHall) {
        super();
        this.id = cinemaHall.id;
        this.movie = new Movie(cinemaHall.movie);
        this.movieSession = new Date(cinemaHall.movieSession.getTime());
        this.seats = new ArrayList<>(cinemaHall.seats);
    }
    public CinemaHall(Long id, Movie movie, Date movieSession, List<Seat> seats) {
        super();
        this.id = id;
        this.movie = new Movie(movie);
        this.movieSession = new Date(movieSession.getTime());
        this.seats = new ArrayList<>(seats);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {return new Movie(movie);}

    public void setMovie(Movie movie) {this.movie = new Movie(movie);}

    public Date getMovieSession() {return new Date(movieSession.getTime());}

    public void setMovieSession(Date movieSession) {this.movieSession = new Date(movieSession.getTime()) ;}

    public List<Seat> getSeats() {return new ArrayList<>(seats);}

    public void setSeats(List<Seat> seats) {
        this.seats = new ArrayList<>(seats);
    }

    @Override
    public String toString() {

        return "CinemaHall(" +
                "id=" + id +
                ", movie=" + movie +
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
                && Objects.equals(movie, cinemaHall.movie)
                && Objects.equals(movieSession, cinemaHall.movieSession)
                && Objects.equals(seats, cinemaHall.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, movieSession, seats);
    }

}
