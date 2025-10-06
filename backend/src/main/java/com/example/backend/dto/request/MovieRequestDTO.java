package com.example.backend.dto.request;

import com.example.backend.model.AbstractMappedEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MovieRequestDTO extends AbstractMappedEntity {
    @NotBlank(message = "Title is not mandatory")
    @Size(min = 1, max = 100, message = "Required characters")
    private String title;


    @NotBlank(message = "Poster is not mandatory")
    private String poster;

    @NotBlank(message = "Overview is not mandatory")
    private String overview;

    @NotBlank(message = "Duration is not mandatory")
    @Min(value = 0, message = "Duration required longer than 0")
    private Integer duration;


    @NotBlank(message = "Genres is not mandatory")
    private List<String> genres = new ArrayList<>();

    @NotBlank(message = "Release Date is not mandatory")
    @Past(message = "Release Date must be in the past")
    private Date releaseDate;

    private String language;

    @NotBlank(message = "Imdb ID is not mandatory")
    private String imdbId;

    @NotBlank(message = "Film ID is not mandatory")
    private String filmId;


}
