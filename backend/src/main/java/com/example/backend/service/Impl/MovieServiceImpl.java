package com.example.backend.service.Impl;

import com.example.backend.dto.shared.MovieDTO;
import com.example.backend.exception.wrapper.MovieExistedException;
import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.model.Movie;
import com.example.backend.repository.Impl.MovieRepositoryImpl;
import com.example.backend.service.MovieService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    public static final String MOVIE_CACHE = "movie";
    private final RestTemplate restTemplate;
    private final MovieRepositoryImpl movieRepository;

    public MovieServiceImpl(RestTemplate restTemplate, MovieRepositoryImpl movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    //    @Cacheable(MOVIE_CACHE)
    @Override
    public List<Movie> findAll(MovieDTO m) {
        return movieRepository.findAll(m);
    }

    @Override
//    @Cacheable(MOVIE_CACHE)
    public Movie findOne(Long id) {
        return movieRepository.findOne(id)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, "Not match found with id " + id));
    }

    @Override
//    @Cacheable(MOVIE_CACHE)
    public Movie findByMovieId(String id) {
        return movieRepository.findByMovieId(id)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, "Not match found with id " + id));
    }

    @Override
    public Movie createWithId(Long id) {
        String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3ZjgxYTVkNmVhYTVmZWViZjEzNWM5MTJjNzQ1YmI0MSIsIm5iZiI6MTc1MzcwMTQ4OC4zNzksInN1YiI6IjY4ODc1YzcwZTA4OGQ1NzhjNzhhNzRhYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zCqZxPPYPUxf-b_MJPIyb4tgaN6F_TM_n3Jn9nK8pM8";
        String url = "https://api.themoviedb.org/3/movie/" + id + "?language=vi-VN";

        var checkExist = movieRepository.findByMovieId(String.valueOf(id));

        if (checkExist.isPresent()) {
            throw new MovieExistedException("This movie is existed");

        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> res = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        JsonNode root = res.getBody();

        Movie movie = new Movie();
        movie.setTitle(root.path("title").asText());
        movie.setDuration(root.path("runtime").asInt());
        movie.setOverview(root.path("overview").asText());
        movie.setImdbId(root.path("imdb_id").asText());
        movie.setFilmId(root.path("id").asText());
        movie.setPoster("https://image.tmdb.org/t/p/original" + root.path("poster_path").asText());
        movie.setReleaseDate(Date.from(
                LocalDate.parse(root.path("release_date").asText()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<String> genres = new ArrayList<String>();
        for (JsonNode genreNode : root.path("genres")) {
            genres.add(genreNode.path("name").asText());
        }
        movie.setGenre(genres);
        movieRepository.save(movie);
        return movie;
    }


    @Override
    public Movie create(MovieDTO m) {

        Movie movie = new Movie();
        movie.setTitle(m.getTitle());
        movie.setDuration(m.getDuration());
        movie.setOverview(m.getOverview());
        movie.setImdbId(m.getImdbId());
        movie.setFilmId(m.getFilmId());
        movie.setPoster(m.getPoster());
        movie.setReleaseDate(m.getReleaseDate());

        List<String> genres = new ArrayList<String>();
        for (String genreNode : m.getGenre()) {
            genres.add(genreNode);
        }
        movie.setGenre(genres);
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public Movie update(Long id, Movie m) {
        movieRepository.findOne(id).orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, "Not match foundd with id " + id));

        return movieRepository.update(m);
    }

    @Override
    public void delete(Long id) {
        movieRepository.delete(id);

    }

}
