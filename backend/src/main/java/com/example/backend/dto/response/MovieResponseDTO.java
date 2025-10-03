package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieResponseDTO extends AbstractMappedEntity {
    private Long id;
    private String title;
    private String poster;
    private String overview;
    private Integer duration;
    private List<String> genres = new ArrayList<>();
    private Date releaseDate;
    private String imdbId;
    private String filmId;

    public MovieResponseDTO() {
        super();
    }

    public MovieResponseDTO(
            Long id,
            String title,
            String poster,
            String overview,
            Integer duration,
            List<String> genres,
            Date releaseDate,
            String imdbId,
            String filmId) {
        super();
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.duration = duration;
        this.genres = new ArrayList<>(genres);
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

    public Integer getDuration() {
        return duration;
    }

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
}
