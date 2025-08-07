package com.example.backend.service.Impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.backend.exception.wrapper.MovieExistedException;
import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.model.Movie;
import com.example.backend.repository.Impl.MovieRepositoryImpl;
import com.example.backend.service.MovieService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class MovieServiceImpl implements MovieService {
    public static final String MOVIE_CACHE = "movie";
    private final RestTemplate restTemplate;
    private final MovieRepositoryImpl movieRepository;

    public MovieServiceImpl(RestTemplate restTemplate, MovieRepositoryImpl movieRepository) {
	this.restTemplate = restTemplate;
	this.movieRepository = movieRepository;
    }

    @Cacheable(value = MOVIE_CACHE, key = "#movie")
    @Override
    public List<Movie> findAll() {
	return movieRepository.findAll();
    }

    @Override
    @Cacheable(value = MOVIE_CACHE, key = "#movie")
    public Movie findOne(Long id) {
	return movieRepository.findOne(id)
		.orElseThrow(() -> new MovieNotFoundException("Not match found with id " + id));
    }

    @Override
    @Cacheable(value = MOVIE_CACHE, key = "#movie")
    public Movie findByMovieId(String id) {
	return movieRepository.findByMovieId(id)
		.orElseThrow(() -> new MovieNotFoundException("Not match found with id " + id));
    }

    @Override
    public Movie create(Long id) {
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
    public Movie update(Long id, Movie m) {
	movieRepository.findOne(id).orElseThrow(() -> new MovieNotFoundException("Not match foundd with id " + id));

	return movieRepository.update(m);
    }

    @Override
    public void delete(Long id) {
	movieRepository.delete(id);

    }

}
