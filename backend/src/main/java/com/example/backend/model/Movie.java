package com.example.backend.model;


import com.example.backend.typeHandler.StringListConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Hidden
@Entity
@Table(name = "movie")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie extends AbstractMappedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "poster")
    private String poster;

    @Column(name = "overview")
    private String overview;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "genres", columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> genres = new ArrayList<>();

    @Column(name = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "film_id")
    private String filmId;

    public Movie() {
        super();
    }

    public Movie(Movie movie) {
        super();
        if (movie == null) return;
        this.id = movie.id;
        this.title = movie.title;
        this.poster = movie.poster;
        this.overview = movie.overview;
        this.duration = movie.duration;
        this.genres = new ArrayList<>(movie.genres);
        this.releaseDate = new Date(movie.releaseDate.getTime());
        this.imdbId = movie.imdbId;
        this.filmId = movie.filmId;
    }

    public Movie(Long id, String title, String poster, String overview, Integer duration, List<String> genres,
                 Date releaseDate, String imdbId, String filmId) {
        super();
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.duration = duration;
        this.genres = new ArrayList<>(genres);
        this.releaseDate = new Date(releaseDate.getTime());
        this.imdbId = imdbId;
        this.filmId = filmId;
    }

    public Movie(String title, String poster, String overview, Integer duration, List<String> genres,
                 Date releaseDate, String imdbId, String filmId) {
        super();
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.duration = duration;
        this.genres = new ArrayList<>(genres);
        this.releaseDate = new Date(releaseDate.getTime());
        this.imdbId = imdbId;
        this.filmId = filmId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getDuration() {return duration;}

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<String> getGenres() {
        return new ArrayList<>(genres);
    }

    public void setGenres(List<String> genres) {
        this.genres = new ArrayList<>(genres);
    }

    public Date getReleaseDate() {
        return new Date(releaseDate.getTime());
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = new Date(releaseDate.getTime());
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    @Override
    public String toString() {

        return "Movie{" + "id=" + id +
                ", title=" + title +
                ", poster='" + poster + '\'' +
                ", overview='" + overview + '\'' +
                ", duration='" + duration + '\'' +
                ", genres='" + genres + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", imdbId='" + imdbId + '\'' + '}' +
                ", filmId='" + filmId + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Movie movie = (Movie) obj;
        return Objects.equals(id, movie.id)
                && Objects.equals(poster, movie.poster)
                && Objects.equals(overview, movie.overview)
                && Objects.equals(duration, movie.duration)
                && Objects.equals(genres, movie.genres)
                && Objects.equals(releaseDate, movie.releaseDate)
                && Objects.equals(filmId, movie.filmId)
                && Objects.equals(imdbId, movie.imdbId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, poster, overview, duration, genres, releaseDate, imdbId, filmId);
    }
}
