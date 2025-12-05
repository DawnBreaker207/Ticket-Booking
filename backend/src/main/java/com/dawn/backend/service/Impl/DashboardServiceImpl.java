package com.dawn.backend.service.Impl;

import com.dawn.backend.dto.request.DashboardFilterRequest;
import com.dawn.backend.dto.response.dashboard.*;
import com.dawn.backend.repository.*;
import com.dawn.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;

    @Override
//    @Cacheable(value = "dashboard:metrics", key = "#req.movieId + ':' + #req.theaterId")
    public MetricsResponse getMetrics(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        Double totalRevenue = dashboardRepository.getTotalRevenue(start, end, req.getMovieId(), req.getTheaterId());
        Long ticketSold = dashboardRepository.getTicketsSold(start, end, req.getMovieId(), req.getTheaterId());
        Long activeTheaters = dashboardRepository.getActiveTheaters(start, end, req.getMovieId(), req.getTheaterId());
        Double seatUtilization = dashboardRepository.getSeatUtilization(start, end, req.getTheaterId());

        return MetricsResponse
                .builder()
                .totalRevenue(totalRevenue)
                .ticketsSold(ticketSold)
                .activeTheaters(activeTheaters)
                .seatUtilization(seatUtilization)
                .build();
    }

    @Override
//    @Cacheable(value = "dashboard:revenue", key = "#req.movieId + ':' + #req.theaterId")
    public List<RevenuePointResponse> getRevenueOverTime(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        return dashboardRepository.getRevenueOverTime(start, end, req.getTheaterId());
    }

    @Override
//    @Cacheable(value = "dashboard:topMovies", key = "#req.movieId + ':' + #req.theaterId")
    public List<TopMovieResponse> getTopMovies(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        return dashboardRepository.getTopMovies(start, end);

    }

    @Override
//    @Cacheable(value = "dashboard:topTheaters", key = "#req.movieId + ':' + #req.theaterId")
    public List<TopTheaterResponse> getTopTheaters(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        return dashboardRepository.getTopTheaters(start, end);

    }

    @Override
//    @Cacheable(value = "dashboard:paymentDistribution", key = "#req.movieId + ':' + #req.theaterId")
    public List<PaymentDistribution> getPaymentDistribution(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        return dashboardRepository.getPaymentDistribution(start, end);

    }

}
