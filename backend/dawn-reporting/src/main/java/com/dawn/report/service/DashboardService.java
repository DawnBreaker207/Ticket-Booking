package com.dawn.backend.service;

import com.dawn.backend.dto.request.DashboardFilterRequest;
import com.dawn.backend.dto.response.dashboard.*;

import java.util.List;


public interface DashboardService {
    MetricsResponse getMetrics(DashboardFilterRequest req);

    List<RevenuePointResponse> getRevenueOverTime(DashboardFilterRequest req);

    List<TopMovieResponse> getTopMovies(DashboardFilterRequest req);

    List<TopTheaterResponse> getTopTheaters(DashboardFilterRequest req);

    List<PaymentDistribution> getPaymentDistribution(DashboardFilterRequest req);
}
