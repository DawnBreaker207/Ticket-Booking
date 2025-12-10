package com.dawn.report.repository;


import com.dawn.report.dto.response.PaymentDistribution;
import com.dawn.report.dto.response.RevenuePointResponse;
import com.dawn.report.dto.response.TopMovieResponse;
import com.dawn.report.dto.response.TopTheaterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Double getTotalRevenue(LocalDate from, LocalDate to, Long movieId, Long theaterId) {
        String sql = """
                SELECT COALESCE(SUM(p.amount), 0) AS totalRevenue
                FROM payment p
                JOIN reservation r ON r.id = p.reservation_id
                JOIN showtime s ON s.id = r.showtime_id
                WHERE p.status = 'PAID'
                    AND (:from IS NULL OR p.created_at >= :from)
                    AND (:to IS NULL OR p.created_at <= :to)
                    AND (:movieId IS NULL OR s.movie_id = :movieId)
                    AND (:theaterId IS NULL OR s.theater_id = :theaterId)
                """;

        return jdbcTemplate.queryForObject(
                sql,
                getParams(from, to, movieId, theaterId),
                Double.class
        );
    }

    public Long getTicketsSold(LocalDate from, LocalDate to, Long movieId, Long theaterId) {
        String sql = """
                SELECT COUNT(se.id) AS ticketsSold
                FROM seat se
                JOIN reservation r ON r.id = se.reservation_id
                JOIN showtime s ON s.id = r.showtime_id
                WHERE r.status = 'CONFIRMED'
                    AND (:from IS NULL OR r.created_at >= :from)
                    AND (:to IS NULL OR r.created_at <= :to)
                	AND (:movieId IS NULL OR s.movie_id = :movieId)
                	AND (:theaterId IS NULL OR s.theater_id = :theaterId)
                """;
        return jdbcTemplate.queryForObject(
                sql,
                getParams(from, to, movieId, theaterId),
                Long.class
        );
    }

    public Long getActiveTheaters(LocalDate from, LocalDate to, Long movieId, Long theaterId) {
        String sql = """
                SELECT COUNT(DISTINCT s.theater_id) AS activeTheaters
                FROM showtime s
                JOIN reservation r ON r.showtime_id = s.id
                WHERE r.status = 'CONFIRMED'
                    AND (:from IS NULL OR r.created_at >= :from)
                    AND (:to IS NULL OR r.created_at <= :to)
                	AND (:movieId IS NULL OR s.movie_id = :movieId)
                	AND (:theaterId IS NULL OR s.theater_id = :theaterId)
                """;
        return jdbcTemplate.queryForObject(
                sql,
                getParams(from, to, movieId, theaterId),
                Long.class
        );
    }

    public Double getSeatUtilization(LocalDate from, LocalDate to, Long theaterId) {
        String sql = """
                SELECT ROUND(AVG(utilization),2) AS seatUtilization
                FROM (
                SELECT
                	CASE
                		WHEN s.total_seats = 0 THEN 0
                		ELSE SUM(CASE WHEN r.id IS NOT NULL THEN 1 ELSE 0 END)
                            / s.total_seats * 100
                	END AS utilization
                FROM showtime s
                LEFT JOIN seat se ON se.showtime_id = s.id
                LEFT JOIN reservation r ON r.id = se.reservation_id AND r.status = 'CONFIRMED'
                WHERE (:theaterId IS NULL OR s.theater_id = :theaterId)
                    AND (:from IS NULL OR s.show_date >= :from)
                    AND (:to IS NULL OR s.show_date <= :to)
                GROUP BY s.id
                ) AS t
                """;
        return jdbcTemplate.queryForObject(
                sql,
                getParams(from, to, null, theaterId),
                Double.class
        );
    }

    public List<RevenuePointResponse> getRevenueOverTime(LocalDate from, LocalDate to, Long theaterId) {
        String sql = """
                SELECT
                    DATE(p.created_at) AS date,
                    COALESCE(SUM(p.amount), 0) AS revenue
                FROM
                    payment p
                JOIN reservation r ON
                    r.id = p.reservation_id
                JOIN showtime s ON
                    r.showtime_id = s.id
                LEFT JOIN seat se ON
                    se.reservation_id = r.id
                WHERE
                    (:from IS NULL OR p.created_at >= :from)
                    AND
                    (:to IS NULL OR p.created_at <= :to)
                    AND
                    (:theaterId IS NULL OR s.theater_id = :theaterId)
                GROUP BY
                    DATE(p.created_at)
                ORDER BY
                    DATE(p.created_at)
                """;
        return jdbcTemplate.query(
                sql,
                getParams(from, to, null, theaterId),
                new BeanPropertyRowMapper<>(RevenuePointResponse.class)
        );
    }

    public List<TopMovieResponse> getTopMovies(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                	m.title AS movieName,
                	COALESCE(COUNT(se.id), 0) AS ticketSold,
                	COALESCE(SUM(p.amount), 0) AS revenue
                FROM
                	movie m
                JOIN showtime s ON
                	s.movie_id = m.id
                LEFT JOIN reservation r ON
                	r.showtime_id = s.id
                	AND r.is_deleted = false
                LEFT JOIN seat se ON
                    se.reservation_id = r.id
                LEFT JOIN payment p ON
                	p.reservation_id = r.id
                WHERE
                	(:from IS NULL OR r.created_at >= :from)
                    AND
                    (:to IS NULL OR r.created_at <= :to)
                GROUP BY
                	m.id,
                	m.title
                ORDER BY
                	ticketSold DESC
                LIMIT 5
                """;
        return jdbcTemplate.query(
                sql,
                getParams(from, to, null, null),
                new BeanPropertyRowMapper<>(TopMovieResponse.class)
        );
    }

    public List<TopTheaterResponse> getTopTheaters(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                	t.name AS theaterName,
                	COALESCE(COUNT(se.id), 0) AS ticketsSold,
                	COALESCE(SUM(p.amount), 0) AS totalRevenue
                FROM
                	theater t
                JOIN showtime s ON
                	s.theater_id = t.id
                LEFT JOIN reservation r ON
                	r.showtime_id = s.id
                	AND r.is_deleted = false
                LEFT JOIN seat se ON
                    se.reservation_id = r.id
                LEFT JOIN payment p ON
                	p.reservation_id = r.id
                WHERE
                	(:from IS NULL OR p.created_at >= :from)
                    AND
                    (:to IS NULL OR p.created_at <= :to)
                GROUP BY
                	t.id,
                	t.name
                ORDER BY
                	ticketsSold DESC
                LIMIT 5
                """;
        return jdbcTemplate.query(
                sql,
                getParams(from, to, null, null),
                new BeanPropertyRowMapper<>(TopTheaterResponse.class)
        );
    }

    public List<PaymentDistribution> getPaymentDistribution(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    p.method AS method,
                    COUNT(r.id) AS count,
                    COALESCE(SUM(p.amount), 0) AS amount
                FROM
                    payment p
                JOIN reservation r ON
                    r.id = p.reservation_id
                WHERE
                    (:from IS NULL OR p.created_at >= :from)
                    AND
                    (:to IS NULL OR p.created_at <= :to)
                GROUP BY
                    p.method
                """;

        return jdbcTemplate.query(
                sql,
                getParams(from, to, null, null),
                new BeanPropertyRowMapper<>(PaymentDistribution.class)
        );
    }

    private MapSqlParameterSource getParams(LocalDate from, LocalDate to, Long movieId, Long theaterId) {
        return new MapSqlParameterSource()
                .addValue("from", from != null ? from.atStartOfDay(): null)
                .addValue("to", to != null ? to.atTime(LocalTime.MAX) : null)
                .addValue("movieId", movieId)
                .addValue("theaterId", theaterId);
    }
}
