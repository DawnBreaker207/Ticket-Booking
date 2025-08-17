package com.example.backend.controller;

import java.util.List;

import com.example.backend.dto.MovieDTO;
import com.example.backend.response.ResponseObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Movie;
import com.example.backend.service.Impl.MovieServiceImpl;

@RestController
@RequestMapping("/movie")
@Tag(name = "Movie", description = "Operations related to movie")
public class MovieController {

    public final MovieServiceImpl movieService;

    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("")
    public ResponseObject<List<Movie>> findAll(@ModelAttribute MovieDTO m) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.findAll(m));
    }

    @GetMapping("/{id}")
    public ResponseObject<Movie> findById(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.findOne(id));
    }

    @GetMapping("/filmId/{id}")
    public ResponseObject<Movie> findByMovieId(@PathVariable String id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.findByMovieId(id));
    }

    @PostMapping("")
    public ResponseObject<Movie> create(@RequestBody MovieDTO m) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.create(m));
    }

    @PostMapping("/filmId/{id}")
    public ResponseObject<Movie> createWithId(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.createWithId(id));
    }

    @PutMapping("/{id}")
    public ResponseObject<Movie> update(@PathVariable Long id, @RequestBody Movie m) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.update(id, m));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

}
