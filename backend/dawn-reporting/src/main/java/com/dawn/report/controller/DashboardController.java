package com.dawn.report.controller;

import com.dawn.common.dto.response.ResponseObject;
import com.dawn.report.dto.request.DashboardFilterRequest;
import com.dawn.report.dto.response.*;
import com.dawn.report.service.DashboardService;
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

    @GetMapping("/summary")
    public ResponseObject<DashboardResponse> summary(@ModelAttribute DashboardFilterRequest req) {
        return ResponseObject.success(dashboardService.getSummary(req));
    }

    @GetMapping("/metrics")
    public ResponseObject<MetricsResponse> getMetrics(@ModelAttribute DashboardFilterRequest request) {
        return ResponseObject.success(dashboardService.getMetrics(request));
    }

    @GetMapping("/revenue")
    public ResponseObject<List<RevenuePointResponse>> getRevenue(@ModelAttribute DashboardFilterRequest request) {
        return ResponseObject.success(dashboardService.getRevenueOverTime(request));
    }

    @GetMapping("/top-movies")
    public ResponseObject<List<TopMovieResponse>> getTopMovies(@ModelAttribute DashboardFilterRequest request) {
        return ResponseObject.success(dashboardService.getTopMovies(request));
    }

    @GetMapping("/top-theaters")
    public ResponseObject<List<TopTheaterResponse>> getTopTheaters(@ModelAttribute DashboardFilterRequest request) {
        return ResponseObject.success(dashboardService.getTopTheaters(request));
    }

    @GetMapping("/payment-distribution")
    public ResponseObject<List<PaymentDistribution>> getPaymentDistribution(@ModelAttribute DashboardFilterRequest request) {
        return ResponseObject.success(dashboardService.getPaymentDistribution(request));
    }

}
