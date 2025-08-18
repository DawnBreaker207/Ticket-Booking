package com.example.backend.controller;

import com.example.backend.dto.shared.CinemaHallDTO;
import com.example.backend.model.CinemaHall;
import com.example.backend.service.Impl.CinemaHallServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cinema")
@Tag(name = "CinemaHall" , description = "Operations related to cinema hall")
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
    public void updateSeats(@PathVariable Long hallId, @RequestBody CinemaHallDTO cinemaHall) {
        System.out.println(cinemaHall.getSeatStatus());
        cinemaHallService.updateSeats(hallId, cinemaHall.getSeatCodes(), cinemaHall.getSeatStatus());
    }

}
