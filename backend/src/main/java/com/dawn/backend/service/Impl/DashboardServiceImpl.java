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
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;

    @Override
//    @Cacheable(value = "dashboard:metrics", key = "#req.movieId + ':' + #req.theaterId")
    public MetricsResponse getMetrics(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        Double totalRevenue = paymentRepository.getTotalRevenue(start, end, req.getMovieId(), req.getTheaterId());
        Long ticketSold = reservationRepository.getTicketsSold(start, end, req.getMovieId(), req.getTheaterId());
        Long activeTheaters = reservationRepository.getActiveTheaters(start, end, req.getMovieId(), req.getTheaterId());
        Double seatUtilization = showtimeRepository.getSeatUtilization(start, end, req.getTheaterId());

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

        List<Object[]> raw = paymentRepository.getRevenueOverTime(start, end, req.getTheaterId());
        return raw.stream().map(
                        r ->
                                RevenuePointResponse
                                        .builder()
                                        .date(((java.sql.Date) r[0]).toLocalDate())
                                        .revenue(((Number) r[1]).longValue())
                                        .build())
                .toList();
    }

    @Override
//    @Cacheable(value = "dashboard:topMovies", key = "#req.movieId + ':' + #req.theaterId")
    public List<TopMovieResponse> getTopMovies(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        List<Object[]> raw = movieRepository.getTopMovie(start, end);
        return raw
                .stream()
                .map(r ->
                        TopMovieResponse
                                .builder()
                                .movieName((String) r[0])
                                .ticketSold(((Number) r[1]).longValue())
                                .revenue(((Number) r[2]).longValue())
                                .build()).collect(Collectors.toList());
    }

    @Override
//    @Cacheable(value = "dashboard:topTheaters", key = "#req.movieId + ':' + #req.theaterId")
    public List<TopTheaterResponse> getTopTheaters(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        List<Object[]> raw = theaterRepository.getTopTheaters(start, end);
        return raw
                .stream()
                .map(r -> TopTheaterResponse
                        .builder()
                        .theaterName((String) r[0])
                        .ticketsSold(((Number) r[1]).longValue())
                        .totalRevenue(((Number) r[2]).longValue())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
//    @Cacheable(value = "dashboard:paymentDistribution", key = "#req.movieId + ':' + #req.theaterId")
    public List<PaymentDistribution> getPaymentDistribution(DashboardFilterRequest req) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        List<Object[]> raw = paymentRepository.getPaymentDistribution(start, end);
        return raw
                .stream()
                .map(r -> PaymentDistribution
                        .builder()
                        .method((String) r[0])
                        .amount(((Number) r[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }

}
