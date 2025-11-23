package com.dawn.backend.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TopMovieDTO {
    private String movieName;
    private Long ticketSold;
    private Long revenue;
}
