package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.MovieRequest;
import com.dawn.backend.dto.response.MovieResponse;
import com.dawn.backend.service.MovieService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
@Tag(name = "Movie", description = "Operations related to movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("")
    @RateLimiter(name = "limiter")
    public ResponseObject<ResponsePage<MovieResponse>> findAll(@ModelAttribute MovieRequest m, Pageable pageable) {
        return ResponseObject.success(movieService.findAll(m, pageable));
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "limiter")
    public ResponseObject<MovieResponse> findById(@PathVariable Long id) {
        return ResponseObject.success(movieService.findOne(id));
    }

    @GetMapping("/filmId/{id}")
    public ResponseObject<MovieResponse> findByMovieId(@PathVariable String id) {
        return ResponseObject.success(movieService.findByMovieId(id));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<MovieResponse> create(@RequestBody MovieRequest m) {
        return ResponseObject.created(movieService.create(m));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<MovieResponse> update(@PathVariable Long id, @RequestBody MovieRequest m) {
        return ResponseObject.success(movieService.update(id, m));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseObject.deleted();
    }

}
