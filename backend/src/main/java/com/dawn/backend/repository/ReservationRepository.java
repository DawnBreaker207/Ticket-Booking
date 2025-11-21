package com.dawn.backend.repository;

import com.dawn.backend.constant.ReservationStatus;
import com.dawn.backend.dto.request.ReservationFilterDTO;
import com.dawn.backend.model.Reservation;
import com.dawn.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    List<Reservation> user(User user);
}

