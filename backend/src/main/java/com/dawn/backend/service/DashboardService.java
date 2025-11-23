package com.dawn.backend.service;

import com.dawn.backend.dto.request.DashboardFilterRequestDTO;
import com.dawn.backend.dto.response.dashboard.*;

import java.util.List;


public interface DashboardService {
    MetricsResponseDTO getMetrics(DashboardFilterRequestDTO req);

    List<RevenuePointDTOResponse> getRevenueOverTime(DashboardFilterRequestDTO req);

    List<TopMovieDTO> getTopMovies(DashboardFilterRequestDTO req);

    List<TopTheaterDTO> getTopTheaters(DashboardFilterRequestDTO req);

    List<PaymentDistributionDTO> getPaymentDistribution(DashboardFilterRequestDTO req);
}
