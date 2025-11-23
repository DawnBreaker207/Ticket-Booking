package com.dawn.backend.service.Impl;

import com.dawn.backend.dto.request.DashboardFilterRequestDTO;
import com.dawn.backend.dto.response.dashboard.*;
import com.dawn.backend.repository.*;
import com.dawn.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public MetricsResponseDTO getMetrics(DashboardFilterRequestDTO req) {
        Double totalRevenue = paymentRepository.getTotalRevenue(req.getFrom(), req.getTo(), req.getMovieId(), req.getTheaterId());
        Long ticketSold = reservationRepository.getTicketsSold(req.getMovieId(), req.getTheaterId());
        Long activeTheaters = reservationRepository.getActiveTheaters(req.getFrom(), req.getTo(), req.getMovieId(), req.getTheaterId());
        Double seatUtilization = showtimeRepository.getSeatUtilization(req.getFrom(), req.getTo(), req.getTheaterId());
        return MetricsResponseDTO
                .builder()
                .totalRevenue(totalRevenue)
                .ticketsSold(ticketSold)
                .activeTheaters(activeTheaters)
                .seatUtilization(seatUtilization)
                .build();
    }

    @Override
    public List<RevenuePointDTOResponse> getRevenueOverTime(DashboardFilterRequestDTO req) {
        List<Object[]> raw = paymentRepository.getRevenueOverTime(req.getFrom(), req.getTo(), req.getTheaterId());
        return raw.stream().map(
                        r ->
                                RevenuePointDTOResponse
                                        .builder()
                                        .date(((java.sql.Date) r[0]).toLocalDate())
                                        .revenue(((Number) r[1]).longValue())
                                        .build())
                .toList();
    }

    @Override
    public List<TopMovieDTO> getTopMovies(DashboardFilterRequestDTO req) {
        List<Object[]> raw = movieRepository.getTopMovie(req.getFrom(), req.getTo());
        return raw
                .stream()
                .map(r ->
                        TopMovieDTO
                                .builder()
                                .movieName((String) r[0])
                                .ticketSold(((Number) r[1]).longValue())
                                .revenue(((Number) r[2]).longValue())
                                .build()).collect(Collectors.toList());
    }

    @Override
    public List<TopTheaterDTO> getTopTheaters(DashboardFilterRequestDTO req) {
        List<Object[]> raw = theaterRepository.getTopTheaters(req.getFrom(), req.getTo());
        return raw
                .stream()
                .map(r -> TopTheaterDTO
                        .builder()
                        .theaterName((String) r[0])
                        .ticketsSold(((Number) r[1]).longValue())
                        .totalRevenue(((Number) r[2]).longValue())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDistributionDTO> getPaymentDistribution(DashboardFilterRequestDTO req) {
        List<Object[]> raw = paymentRepository.getPaymentDistribution(req.getFrom(), req.getTo());
        return raw
                .stream()
                .map(r -> PaymentDistributionDTO
                        .builder()
                        .method((String) r[0])
                        .amount(((Number) r[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }

}
