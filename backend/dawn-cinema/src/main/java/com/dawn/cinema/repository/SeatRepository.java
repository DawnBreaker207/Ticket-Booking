package com.dawn.backend.repository;

import com.dawn.backend.constant.SeatStatus;
import com.dawn.backend.model.Seat;
import com.dawn.backend.model.Showtime;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    //    Find seats by IDs with pessimistic write lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    List<Seat> findByIdWithLock(List<Long> seatIds);

    List<Seat> findByShowtime(Showtime showtime);

    List<Seat> findByShowtimeAndStatus(Showtime showtime, SeatStatus seatStatus);


}
