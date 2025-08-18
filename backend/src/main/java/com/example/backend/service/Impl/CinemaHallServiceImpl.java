package com.example.backend.service.Impl;

import com.example.backend.constant.SeatStatus;
import com.example.backend.exception.wrapper.CinemaHallNotFoundException;
import com.example.backend.model.CinemaHall;
import com.example.backend.repository.Impl.CinemaHallRepositoryImpl;
import com.example.backend.service.CinemaHallService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
    private final CinemaHallRepositoryImpl cinemaHallRepository;

    public CinemaHallServiceImpl(CinemaHallRepositoryImpl cinemaHallRepository) {
	this.cinemaHallRepository = cinemaHallRepository;
    }

    @Override
    public List<CinemaHall> findAll() {
	return cinemaHallRepository.findAll();
    }

    @Override
    public CinemaHall findOne(Long id) {
	return cinemaHallRepository.findOne(id).orElseThrow(() -> new CinemaHallNotFoundException("Can not find with id " + id));
    }

    @Override
    public CinemaHall findByMovieAndSession(Long movieId, String movieSession) {
	return cinemaHallRepository.findByMovieIdAndMovieSession(movieId, movieSession)
		.orElseThrow(() -> new CinemaHallNotFoundException("Can not find with movie id " + movieId));
    }

    @Override
    public CinemaHall createMovieSchedule(CinemaHall cinemaHall) {
	return cinemaHallRepository.save(cinemaHall);
    }

    @Override
    public CinemaHall updateMovieSchedule(Long id, CinemaHall cinemaHall) {
	cinemaHallRepository.findOne(id).orElseThrow(() -> new CinemaHallNotFoundException("Can not find with movie id " + id));

	cinemaHall.setId(id);
	return cinemaHallRepository.update(cinemaHall);
    }

    @Override
    public void removeMovieSchedule(Long id) {
	cinemaHallRepository.delete(id);
    }

    @Override
    public void updateSeats(Long hallId, List<String> seatCodes, SeatStatus seatStatus) {
	System.out.println(seatStatus);
	cinemaHallRepository.findOne(hallId).orElseThrow(() -> new CinemaHallNotFoundException("Can not find with id " + hallId));

	cinemaHallRepository.updateSeatStatus(seatCodes, seatStatus);

    }

}
