package com.dawn.backend.repository;

import com.dawn.backend.constant.ReservationStatus;
import com.dawn.backend.dto.request.ReservationFilterDTO;
import com.dawn.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query(value = """
              SELECT DISTINCT r FROM Reservation AS r
                   LEFT JOIN FETCH r.user AS u
                   LEFT JOIN FETCH r.showtime AS st
                   LEFT JOIN FETCH st.movie as m
                   LEFT JOIN FETCH st.theater AS ch
                   LEFT JOIN FETCH r.seats AS os
                   WHERE
                         (:#{#reservation.getQuery()} IS NULL OR r.id LIKE CONCAT('%', :#{#reservation.getQuery()}, '%') )
                         AND (:#{#reservation.getUsername()} IS NULL OR u.username =:#{#reservation.getUsername()})
                         AND (:#{#reservation.getReservationStatus()} IS NULL OR r.reservationStatus = :#{#reservation.getReservationStatus()})
                         AND (
                            :#{#reservation.getStartDate()} IS NULL
                            OR :#{#reservation.getEndDate()} IS NULL
                            OR ( r.createdAt  BETWEEN :#{#reservation.getStartDate()} AND :#{#reservation.getEndDate()}) )
                         AND (:#{#reservation.getTotalAmount()} IS NULL OR r.totalAmount = :#{#reservation.getTotalAmount()})
                         AND (:#{#reservation.getIsPaid()} IS NULL OR r.isPaid = :#{#reservation.getIsPaid()})
                    ORDER BY
                        CASE WHEN :#{#reservation.getSortBy()} = 'oldest' THEN r.createdAt END ASC,
                        CASE WHEN :#{#reservation.getSortBy()} = 'newest' THEN r.createdAt END DESC
            """)
    List<Reservation> findAllWithFilter(ReservationFilterDTO reservation);

    List<Reservation> findAllByUserIdAndIsPaidAndReservationStatusOrderByCreatedAtDesc(Long userId, Boolean isPaid, ReservationStatus status);

    @Query(value = """
            SELECT
            	COUNT(se.id)
            FROM
            	seat se
            JOIN reservation r ON
            	r.id = se.reservation_id
            JOIN showtime s ON
            	s.id = r.showtime_id
            WHERE
            	r.status = 'CONFIRMED'
            	AND (:movieId IS NULL
            		OR s.movie_id = :movieId)
            	AND (:theaterId IS NULL
            		OR s.theater_id = :theaterId)
            """, nativeQuery = true)
    Long getTicketsSold(
            @Param("movieId") Long movieId,
            @Param("theaterId") Long theaterId);

    @Query(value = """
            SELECT
            	COUNT(DISTINCT s.theater_id)
            FROM
            	showtime s
            JOIN reservation r ON
            	r.showtime_id = s.id
            WHERE
            	r.status = 'CONFIRMED'
                AND
                (:from IS NULL OR r.created_at >= :from)
                AND
                (:to IS NULL OR r.created_at <= :to)
            	AND (:movieId IS NULL
            		OR s.movie_id = :movieId)
            	AND (:theaterId IS NULL
            		OR s.theater_id = :theaterId)
            """, nativeQuery = true)
    Long getActiveTheaters(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("movieId") Long movieId,
            @Param("theaterId") Long theaterId);
}

