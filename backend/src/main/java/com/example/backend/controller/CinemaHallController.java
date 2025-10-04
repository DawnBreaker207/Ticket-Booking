package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.response.CinemaHallResponseDTO;
import com.example.backend.model.CinemaHall;
import com.example.backend.service.Impl.CinemaHallServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cinema")
@Tag(name = "CinemaHall", description = "Operations related to cinema hall")
public class CinemaHallController {

    private final CinemaHallServiceImpl cinemaHallService;

    public CinemaHallController(CinemaHallServiceImpl cinemaHallService) {
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping("")
    public ResponseObject<List<CinemaHallResponseDTO>> findAll() {
        return new ResponseObject<>(HttpStatus.OK, "Success", cinemaHallService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseObject<CinemaHall> findOne(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", cinemaHallService.findOne(id));
    }

    @GetMapping("/movie")
    public ResponseObject<CinemaHall> findByMovieAndSessionName(@RequestParam(required = false) Long movieId,
                                                                @RequestParam(required = false) Date movieSession) {
        return new ResponseObject<>(HttpStatus.OK, "Success", cinemaHallService.findByMovieAndSession(movieId, movieSession));
    }

    @PostMapping("")
    public ResponseObject<CinemaHall> createMovieSchedule(@RequestBody CinemaHall cinema) {
        System.out.println(cinema.getMovie().getId());
        System.out.println(cinema.getMovie());
        return new ResponseObject<>(HttpStatus.OK, "Success", cinemaHallService.createMovieSchedule(cinema));
    }

    @PutMapping("/{id}")
    public ResponseObject<CinemaHall> updateMovieSchedule(@PathVariable Long id, @RequestBody CinemaHall cinemaHall) {
        return new ResponseObject<>(HttpStatus.OK, "Success", cinemaHallService.updateMovieSchedule(id, cinemaHall));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMovieSchedule(@PathVariable Long id) {
        cinemaHallService.removeMovieSchedule(id);
    }

}
