package com.dawn.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopMovieResponse {
    private String movieName;
    private Long ticketSold;
    private Long revenue;
}
