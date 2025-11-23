package com.dawn.backend.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricsResponseDTO {
    private Double totalRevenue;
    private Long ticketsSold;
    private Long activeTheaters;
    private Double seatUtilization;
}
