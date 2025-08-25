package com.example.backend.dto.shared;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Movie;

import java.util.Date;
import java.util.List;

public class CinemaHallDTO extends AbstractMappedEntity {
    public Long id;
    public Date movieSession;
    public Movie movie;
    private List<String> seatCodes;
    private SeatStatus seatStatus;

    public CinemaHallDTO() {}


    public CinemaHallDTO(Long id, Date movieSession, Movie movie, List<String> seatCodes, SeatStatus seatStatus) {
        this.id = id;
        this.movieSession = movieSession;
        this.movie = movie;
        this.seatCodes = seatCodes;
        this.seatStatus = seatStatus;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Date getMovieSession() {return movieSession;}

    public void setMovieSession(Date movieSession) {
        this.movieSession = movieSession;
    }

    public Movie getMovie() {return movie;}

    public void setMovie(Movie movie) {this.movie = movie;}

    public List<String> getSeatCodes() {
        return seatCodes;
    }

    public void setSeatCodes(List<String> seatCodes) {
        this.seatCodes = seatCodes;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }
}
