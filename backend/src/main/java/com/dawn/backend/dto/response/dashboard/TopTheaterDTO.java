package com.dawn.backend.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TopTheaterDTO {
    private String theaterName;
    private Long ticketsSold;
    private Long totalRevenue;
}
