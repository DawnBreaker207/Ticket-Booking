package com.example.backend.service.Impl;

import com.example.backend.constant.SeatStatus;
import com.example.backend.exception.wrapper.CinemaHallNotFoundException;
import com.example.backend.model.CinemaHall;
import com.example.backend.model.Seat;
import com.example.backend.repository.CinemaHallRepository;
import com.example.backend.service.CinemaHallService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
    private final CinemaHallRepository cinemaHallRepository;

    public CinemaHallServiceImpl(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    @Override
    public List<CinemaHall> findAll() {
        return cinemaHallRepository.findAll();
    }

    @Override
    public CinemaHall findOne(Long id) {
        return cinemaHallRepository.findById(id).orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Can not find with id " + id));
    }

    @Override
    public CinemaHall findByMovieAndSession(Long movieId, String movieSession) {
        return cinemaHallRepository.findByMovieIdAndMovieSession(movieId, movieSession)
                .orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Can not find with movie id " + movieId));
    }

    @Override
    @Transactional
    public CinemaHall createMovieSchedule(CinemaHall cinema) {
        cinemaHallRepository.save(cinema);
        insertSeats(cinema);
        return cinemaHallRepository.findById(cinema.getId()).orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Cinema hall not found after create"));
    }

    @Override
    @Transactional
    public CinemaHall updateMovieSchedule(Long id, CinemaHall cinemaHall) {
        cinemaHallRepository.findById(id).orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Can not find with movie id " + id));

        cinemaHall.setId(id);

        cinemaHallRepository.update(cinemaHall);
        return cinemaHall;
    }

    @Override
    public void removeMovieSchedule(Long id) {
        cinemaHallRepository.delete(id);
    }

    @Override
    @Transactional
    public void updateSeats(Long hallId, List<String> seatCodes, SeatStatus seatStatus) {
        cinemaHallRepository.findById(hallId).orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Can not find with id " + hallId));
        cinemaHallRepository.updateSeatStatus(seatCodes, seatStatus);

    }

    private void insertSeats(CinemaHall cinemaHall) {
        List<Seat> seats = new ArrayList<>();
        for (char row = 'A'; row <= 'E'; row++) {
            for (int i = 1; i <= 10; i++) {
                Seat seat = new Seat();
                seat.setSeatNumber(row + String.valueOf(i));
                seat.setCinemaHallId(cinemaHall.getId());
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setPrice(BigDecimal.valueOf(50000));
                seats.add(seat);
            }
        }
        cinemaHallRepository.insertSeats(seats);
    }

}
