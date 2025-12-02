package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.dto.request.DashboardFilterRequest;
import com.dawn.backend.dto.response.dashboard.*;
import com.dawn.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseObject<MetricsResponse> getMetrics(@ModelAttribute DashboardFilterRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", dashboardService.getMetrics(request));
    }

    @GetMapping("/revenue")
    public ResponseObject<List<RevenuePointResponse>> getRevenue(@ModelAttribute DashboardFilterRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", dashboardService.getRevenueOverTime(request));
    }

    @GetMapping("/top-movies")
    public ResponseObject<List<TopMovieResponse>> getTopMovies(@ModelAttribute DashboardFilterRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", dashboardService.getTopMovies(request));
    }

    @GetMapping("/top-theaters")
    public ResponseObject<List<TopTheaterResponse>> getTopTheaters(@ModelAttribute DashboardFilterRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", dashboardService.getTopTheaters(request));
    }

    @GetMapping("/payment-distribution")
    public ResponseObject<List<PaymentDistribution>> getPaymentDistribution(@ModelAttribute DashboardFilterRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", dashboardService.getPaymentDistribution(request));
    }

}
