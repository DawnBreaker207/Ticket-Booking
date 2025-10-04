package com.example.backend.dto.request;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.AbstractMappedEntity;
import com.example.backend.model.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CinemaHallRequestDTO extends AbstractMappedEntity {
    public Long id;
    public Date movieSession;
    public Movie movie;
    private List<String> seatCodes = new ArrayList<>();
    private SeatStatus seatStatus;

    public CinemaHallRequestDTO() {
        super();
    }


    public CinemaHallRequestDTO(Long id, Date movieSession, Movie movie, List<String> seatCodes, SeatStatus seatStatus) {
        super();
        this.id = id;
        this.movieSession = movieSession;
        this.movie = movie;
        this.seatCodes = seatCodes;
        this.seatStatus = seatStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMovieSession() {
        return movieSession;
    }

    public void setMovieSession(Date movieSession) {
        this.movieSession = movieSession;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<String> getSeatCodes() {
        return seatCodes;
    }

    public void setSeatCodes(List<String> seatCodes) {
        this.seatCodes = seatCodes;
    }

    public SeatStatus getStatus() {
        return seatStatus;
    }

    public void setStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }
}
