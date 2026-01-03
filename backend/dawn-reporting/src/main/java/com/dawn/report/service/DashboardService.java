package com.dawn.report.service;


import com.dawn.report.dto.request.DashboardFilterRequest;
import com.dawn.report.dto.response.*;

import java.util.List;


public interface DashboardService {
    DashboardResponse getSummary(DashboardFilterRequest req);

    MetricsResponse getMetrics(DashboardFilterRequest req);

    List<RevenuePointResponse> getRevenueOverTime(DashboardFilterRequest req);

    List<TopMovieResponse> getTopMovies(DashboardFilterRequest req);

    List<TopTheaterResponse> getTopTheaters(DashboardFilterRequest req);

    List<PaymentDistribution> getPaymentDistribution(DashboardFilterRequest req);
}
