package com.dawn.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private MetricsResponse metrics;
    private List<RevenuePointResponse> revenues;
    private List<TopMovieResponse> movies;
    private List<TopTheaterResponse> theaters;
    private List<PaymentDistribution> payments;
}
