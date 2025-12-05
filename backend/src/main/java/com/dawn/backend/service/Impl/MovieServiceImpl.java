package com.dawn.backend.service.Impl;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.constant.Message;
import com.dawn.backend.dto.request.MovieRequest;
import com.dawn.backend.dto.response.MovieResponse;
import com.dawn.backend.exception.wrapper.MovieExistedException;
import com.dawn.backend.exception.wrapper.MovieNotFoundException;
import com.dawn.backend.helper.MovieMappingHelper;
import com.dawn.backend.model.Genre;
import com.dawn.backend.model.Movie;
import com.dawn.backend.repository.GenreRepository;
import com.dawn.backend.repository.MovieRepository;
import com.dawn.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
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
    public static final String CACHE_INFO = "movie_info";
    public static final String CACHE_LIST = "movie_list";
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;


    @Override
    @Cacheable(value = CACHE_LIST, key = "T(java.util.Objects).hash(#m) + '-'+ #pageable.pageNumber + '-'+ #pageable.pageSize")
    public ResponsePage<MovieResponse> findAll(MovieRequest m, Pageable pageable) {
        return new ResponsePage<>(movieRepository
                .findAllWithFilter(m, pageable)
                .map(MovieMappingHelper::map));
    }

    @Override
    @Cacheable(value = CACHE_INFO, key = "#id")
    public MovieResponse findOne(Long id) {
        return movieRepository
                .findById(id)
                .map(MovieMappingHelper::map)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
    }

    @Override
    @Cacheable(value = CACHE_INFO, key = "'filmId:' + #id")
    public MovieResponse findByMovieId(String id) {
        return movieRepository
                .findByFilmId(id)
                .map(MovieMappingHelper::map)
                .orElseThrow(() -> new MovieNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.MOVIE_NOT_FOUND));
    }

    @Override
    @Transactional
    @Caching(
            put = {@CachePut(value = CACHE_INFO, key = "#result.id")},
            evict = {@CacheEvict(value = CACHE_LIST, allEntries = true)}
    )
    public MovieResponse create(MovieRequest m) {
        movieRepository
                .findByFilmId(String.valueOf(m.getFilmId()))
                .ifPresent((movie) -> {
                    throw new MovieExistedException(Message.Exception.MOVIE_EXISTED);
                });
        Set<Genre> genres = checkExistedGenre(m.getGenres());
        Movie movie = MovieMappingHelper.map(m);
        movie.setGenres(genres);
        return MovieMappingHelper.map(movieRepository.save(movie));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_INFO, key = "#id"),
            @CacheEvict(value = CACHE_LIST, allEntries = true),
    })
    public MovieResponse update(Long id, MovieRequest movieDetails) {

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
        return MovieMappingHelper.map(movieRepository.save(movie));

    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_INFO, key = "#id"),
            @CacheEvict(value = CACHE_LIST, allEntries = true)
    })
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
