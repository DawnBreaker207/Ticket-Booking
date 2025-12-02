package com.dawn.backend.repository;

import com.dawn.backend.model.Payment;
import com.dawn.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //    Find payment by reservation
    Optional<Payment> findByReservation(Reservation reservation);

    @Query(value = """
            SELECT
            	COALESCE(SUM(p.amount), 0) AS totalRevenue
            FROM
            	payment p
            JOIN reservation r ON
            	r.id = p.reservation_id
            JOIN showtime s ON
            	s.id = r.showtime_id
            JOIN seat se ON
            	se.reservation_id = r.id
            WHERE
            	p.status = 'PAID'
            	AND
                (:from IS NULL OR p.created_at >= :from)
                AND
                (:to IS NULL OR p.created_at <= :to)
            	AND (:movieId IS NULL
            		OR s.movie_id = :movieId)
            	AND (:theaterId IS NULL
            		OR s.theater_id = :theaterId)
            """, nativeQuery = true)
    Double getTotalRevenue(@Param("from") LocalDate from,
                           @Param("to") LocalDate to,
                           @Param("movieId") Long movieId,
                           @Param("theaterId") Long theaterId);


    @Query(value = """
            SELECT
            	DATE(p.created_at) AS date,
            	COALESCE(SUM(p.amount), 0) AS totalRevenue
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
            """, nativeQuery = true)
    List<Object[]> getRevenueOverTime(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("theaterId") Long theaterId);

    @Query(value = """
            SELECT
            	p.method AS method,
            	COUNT(r.id) AS count,
            	COALESCE(SUM(p.amount), 0) AS totalRevenue
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
            """, nativeQuery = true)
    List<Object[]> getPaymentDistribution(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
