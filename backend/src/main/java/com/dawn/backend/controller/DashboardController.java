package com.dawn.backend.controller;

import com.dawn.backend.dto.request.DashboardFilterRequestDTO;
import com.dawn.backend.dto.response.dashboard.*;
import com.dawn.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public MetricsResponseDTO getMetrics(@ModelAttribute DashboardFilterRequestDTO request) {
        return dashboardService.getMetrics(request);
    }

    @GetMapping("/revenue")
    public List<RevenuePointDTOResponse> getRevenue(@ModelAttribute DashboardFilterRequestDTO request) {
        return dashboardService.getRevenueOverTime(request);
    }

    @GetMapping("/top-movies")
    public List<TopMovieDTO> getTopMovies(@ModelAttribute DashboardFilterRequestDTO request) {
        return dashboardService.getTopMovies(request);
    }

    @GetMapping("/top-theaters")
    public List<TopTheaterDTO> getTopTheaters(@ModelAttribute DashboardFilterRequestDTO request) {
        return dashboardService.getTopTheaters(request);
    }

    @GetMapping("/payment-distribution")
    public List<PaymentDistributionDTO> getPaymentDistribution(@ModelAttribute DashboardFilterRequestDTO request) {
        return dashboardService.getPaymentDistribution(request);
    }

}
