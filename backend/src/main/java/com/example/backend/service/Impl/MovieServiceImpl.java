package com.example.backend.service.Impl;

import com.example.backend.constant.Message;
import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.exception.wrapper.MovieExistedException;
import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.helper.MovieMappingHelper;
import com.example.backend.model.Movie;
import com.example.backend.repository.MovieRepository;
import com.example.backend.service.MovieService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    public static final String MOVIE_CACHE = "movie";
    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;


    //    @Cacheable(MOVIE_CACHE)
    @Override
    public List<MovieResponseDTO> findAll(MovieRequestDTO m) {
        return movieRepository
                .findAllWithFilter(m)
                .stream()
                .map(MovieMappingHelper::map)
                .toList();
    }

    @Override
    @Cacheable(value = MOVIE_CACHE, key = "#id")
    public MovieResponseDTO findOne(Long id) {
        return movieRepository
                .findById(id)
                .map(MovieMappingHelper::map)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
    }

    @Override
    @Cacheable(value = MOVIE_CACHE, key = "#id")
    public MovieResponseDTO findByMovieId(String id) {
        return movieRepository
                .findByFilmId(id)
                .map(MovieMappingHelper::map)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
    }

    @Override
    @Transactional
    public MovieResponseDTO createWithId(Long id) {
        String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3ZjgxYTVkNmVhYTVmZWViZjEzNWM5MTJjNzQ1YmI0MSIsIm5iZiI6MTc1MzcwMTQ4OC4zNzksInN1YiI6IjY4ODc1YzcwZTA4OGQ1NzhjNzhhNzRhYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zCqZxPPYPUxf-b_MJPIyb4tgaN6F_TM_n3Jn9nK8pM8";
        String url = "https://api.themoviedb.org/3/movie/" + id + "?language=vi-VN";

        movieRepository.findByFilmId(String.valueOf(id)).ifPresent((movie) -> {
            throw new MovieExistedException(Message.Exception.MOVIE_EXISTED);
        });

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

        List<String> genres = new ArrayList<>();
        for (JsonNode genreNode : root.path("genres")) {
            genres.add(genreNode.path("name").asText());
        }
        movie.setGenres(genres);
        movieRepository.save(movie);
        return MovieMappingHelper.map(movie);
    }


    @Override
    @Transactional
    public MovieResponseDTO create(MovieRequestDTO m) {
        movieRepository
                .findByFilmId(String.valueOf(m.getFilmId()))
                .ifPresent((movie) -> {
                    throw new MovieExistedException(Message.Exception.MOVIE_EXISTED);
                });
        Movie movie = Movie
                .builder()
                .title(m.getTitle())
                .duration(m.getDuration())
                .overview(m.getOverview())
                .imdbId(m.getImdbId())
                .filmId(m.getFilmId())
                .poster(m.getPoster())
                .language(m.getLanguage())
                .releaseDate(m.getReleaseDate())
                .genres(m.getGenres())
                .build();
        movie.markCreated();
        return MovieMappingHelper.map(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public MovieResponseDTO update(Long id, Movie movie) {
        movieRepository
                .findById(id)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
        return MovieMappingHelper.map(movieRepository.save(movie));

    }

    @Override
    public void delete(Long id) {
        movieRepository
                .findById(id)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));

        movieRepository.deleteById(id);
    }

}
