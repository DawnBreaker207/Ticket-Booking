package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
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


}
