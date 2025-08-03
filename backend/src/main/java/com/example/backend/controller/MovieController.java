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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Movie;
import com.example.backend.service.Impl.MovieServiceImpl;

@RestController
@RequestMapping("/movie")
public class MovieController {

    public final MovieServiceImpl movieService;

    public MovieController(MovieServiceImpl movieService) {
	this.movieService = movieService;
    }

    @GetMapping("")
    public ResponseEntity<List<Movie>> findAll() {
	return ResponseEntity.status(HttpStatus.OK).body(movieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findById(@PathVariable Long id) {
	return ResponseEntity.status(HttpStatus.OK).body(movieService.findOne(id));
    }
    
    @GetMapping("/filmId/{id}")
    public ResponseEntity<Movie> findByMovieId(@PathVariable String id) {
	return ResponseEntity.status(HttpStatus.OK).body(movieService.findByMovieId(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Movie> create(@PathVariable Long id) {
	return ResponseEntity.status(HttpStatus.OK).body(movieService.create(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @RequestBody Movie m) {
	return ResponseEntity.status(HttpStatus.OK).body(movieService.update(id, m));
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id) {
	 movieService.delete(id);
    }

}
