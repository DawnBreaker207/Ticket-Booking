package com.dawn.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopTheaterResponse {
    private String theaterName;
    private Long ticketsSold;
    private Long totalRevenue;
}
