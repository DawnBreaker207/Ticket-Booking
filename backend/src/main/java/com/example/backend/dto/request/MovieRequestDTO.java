package com.example.backend.dto.request;

import com.example.backend.model.AbstractMappedEntity;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieRequestDTO extends AbstractMappedEntity {
    @NotBlank(message = "Title is not mandatory")
    @Size(min = 1, max = 100, message = "Required characters")
    private String title;


    @NotBlank(message = "Poster is not mandatory")
    private String poster;

    @NotBlank(message = "Overview is not mandatory")
    private String overview;

    @NotBlank(message = "Duration is not mandatory")
    @Min(value= 0, message = "Duration required longer than 0")
    private Integer duration;


    @NotBlank(message = "Genres is not mandatory")
    private List<String> genres = new ArrayList<>();

    @NotBlank(message = "Release Date is not mandatory")
    @Past(message = "Release Date must be in the past")
    private Date releaseDate;


    @NotBlank(message = "Imdb ID is not mandatory")
    private String imdbId;

    @NotBlank(message = "Film ID is not mandatory")
    private String filmId;

    public MovieRequestDTO() {
        super();
    }

    public MovieRequestDTO(
            String title,
            String poster,
            String overview,
            Integer duration,
            List<String> genres,
            Date releaseDate,
            String imdbId,
            String filmId) {
        super();
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.duration = duration;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.imdbId = imdbId;
        this.filmId = filmId;
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
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
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
