package com.dawn.backend.repository;

import com.dawn.backend.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Page<Theater> findAll(Pageable pageable);
    //  Find theaters by location (city, area, etc)
    Page<Theater> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    @Query(value = """
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
            """, nativeQuery = true)
    List<Object[]> getTopTheaters(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
