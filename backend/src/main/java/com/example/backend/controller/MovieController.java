package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.model.Movie;
import com.example.backend.service.Impl.MovieServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@Tag(name = "Movie", description = "Operations related to movie")
public class MovieController {

    public final MovieServiceImpl movieService;

    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping("")
    public ResponseObject<List<Movie>> findAll(@ModelAttribute MovieRequestDTO m) {
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<Movie> create(@RequestBody MovieRequestDTO m) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.create(m));
    }

    @PostMapping("/filmId/{id}")
    public ResponseObject<Movie> createWithId(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.createWithId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<Movie> update(@PathVariable Long id, @RequestBody Movie m) {
        return new ResponseObject<>(HttpStatus.OK, "Success", movieService.update(id, m));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

}
