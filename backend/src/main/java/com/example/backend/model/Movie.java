package com.example.backend.model;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Hidden
public class Movie {
    private Long id;
    private String title;
    private String poster;
    private String overview;
    private int duration;
    private List<String> genre;
    private Date releaseDate;
    private String imdbId;
    private String filmId;

    public Movie() {

    }

    public Movie(Long id, String title, String poster, String overview, int duration, List<String> genre,
                 Date releaseDate, String imdbId, String filmId) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.duration = duration;
        this.genre = genre;
        this.releaseDate = releaseDate;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
                ", genre='" + genre + '\'' +
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
                && Objects.equals(genre, movie.genre)
                && Objects.equals(releaseDate, movie.releaseDate)
                && Objects.equals(filmId, movie.filmId)
                && Objects.equals(imdbId, movie.imdbId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, poster, overview, duration, genre, releaseDate, imdbId, filmId);
    }
}
