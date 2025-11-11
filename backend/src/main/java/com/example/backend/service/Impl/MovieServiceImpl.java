package com.example.backend.service.Impl;

import com.example.backend.constant.Message;
import com.example.backend.dto.request.MovieRequestDTO;
import com.example.backend.dto.response.MovieResponseDTO;
import com.example.backend.exception.wrapper.MovieExistedException;
import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.helper.MovieMappingHelper;
import com.example.backend.model.Genre;
import com.example.backend.model.Movie;
import com.example.backend.repository.GenreRepository;
import com.example.backend.repository.MovieRepository;
import com.example.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    public static final String MOVIE_CACHE = "movie";
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

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
//    @Cacheable(value = MOVIE_CACHE, key = "'id:' + #id")
    public MovieResponseDTO findOne(Long id) {
        return movieRepository
                .findById(id)
                .map(MovieMappingHelper::map)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
    }

    @Override
//    @Cacheable(value = MOVIE_CACHE, key = "'movieId' + #id")
    public MovieResponseDTO findByMovieId(String id) {
        return movieRepository
                .findByFilmId(id)
                .map(MovieMappingHelper::map)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
    }

    @Override
    @Transactional
    @CachePut(value = MOVIE_CACHE, key = "'id:' + #result.id")
    public MovieResponseDTO create(MovieRequestDTO m) {
        movieRepository
                .findByFilmId(String.valueOf(m.getFilmId()))
                .ifPresent((movie) -> {
                    throw new MovieExistedException(Message.Exception.MOVIE_EXISTED);
                });
        Set<Genre> genres = checkExistedGenre(m.getGenres());
        Movie movie = MovieMappingHelper.map(m);
        movie.setGenres(genres);
        movie.markCreated();
        return MovieMappingHelper.map(movieRepository.save(movie));
    }

    @Override
    @Transactional
    @CachePut(value = MOVIE_CACHE, key = "'id:' + #id")
    public MovieResponseDTO update(Long id, MovieRequestDTO movieDetails) {

        Movie movie = movieRepository
                .findById(id)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));

        Set<Genre> genres = checkExistedGenre(movieDetails.getGenres());

        movie.setTitle(movieDetails.getTitle());
        movie.setPoster(movieDetails.getPoster());
        movie.setOverview(movieDetails.getOverview());
        movie.setDuration(movieDetails.getDuration());
        movie.setGenres(genres);
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setFilmId(movieDetails.getFilmId());
        movie.setImdbId(movieDetails.getImdbId());
        movie.markUpdated();
        return MovieMappingHelper.map(movieRepository.save(movie));

    }

    @Override
    @CacheEvict(value = MOVIE_CACHE, key = "'id:' + #id")
    public void delete(Long id) {
        movieRepository
                .findById(id)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));

        movieRepository.deleteById(id);
    }

    private Set<Genre> checkExistedGenre(Set<String> genreNames) {
        List<Genre> existedGenres = genreRepository.findAll();

        Map<String, Genre> existingNames = existedGenres
                .stream()
                .collect(Collectors
                        .toMap(Genre::getName, g -> g));

        Set<Genre> result = new HashSet<>();
        for (String name : genreNames) {
            Genre genre = existingNames.get(name);
            if (genre == null) {
                genre = genreRepository.save(Genre.builder().name(name).build());
            }
            result.add(genre);
        }
        return result;
    }
}
