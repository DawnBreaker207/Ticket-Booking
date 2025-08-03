package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CinemaHallUpdateDTO;
import com.example.backend.model.CinemaHall;
import com.example.backend.service.Impl.CinemaHallServiceImpl;

@RestController
@RequestMapping("/cinema")
public class CinemaHallController {

    private CinemaHallServiceImpl cinemaHallService;

    public CinemaHallController(CinemaHallServiceImpl cinemaHallService) {
	this.cinemaHallService = cinemaHallService;
    }

    @GetMapping("")
    public ResponseEntity<List<CinemaHall>> findAll() {
	return ResponseEntity.status(HttpStatus.OK).body(cinemaHallService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaHall> findOne(@PathVariable Long id) {
	return ResponseEntity.status(HttpStatus.OK).body(cinemaHallService.findOne(id));
    }

    @GetMapping("/movie")
    public ResponseEntity<CinemaHall> findByMovieAndSessionName(@RequestParam(required = false) Long movieId,
	    @RequestParam(required = false) String movieSession) {
	return ResponseEntity.status(HttpStatus.OK)
		.body(cinemaHallService.findByMovieAndSession(movieId, movieSession));
    }

    @PostMapping("")
    public ResponseEntity<CinemaHall> createMovieSchedule(@RequestBody CinemaHall cinemaHall) {
	return ResponseEntity.status(HttpStatus.OK).body(cinemaHallService.createMovieSchedule(cinemaHall));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CinemaHall> updateMovieSchedule(@PathVariable Long id, @RequestBody CinemaHall cinemaHall) {
	return ResponseEntity.status(HttpStatus.OK).body(cinemaHallService.updateMovieSchedule(id, cinemaHall));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMovieSchedule(@PathVariable Long id) {
	cinemaHallService.removeMovieSchedule(id);
    }

    @PutMapping("/seat/{hallId}")
    public void updateSeats(@PathVariable Long hallId, @RequestBody CinemaHallUpdateDTO cinemaHall) {
	System.out.println(cinemaHall.getSeatStatus());
	cinemaHallService.updateSeats(hallId, cinemaHall.getSeatCodes(), cinemaHall.getSeatStatus());
    }

}
