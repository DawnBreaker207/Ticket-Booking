package com.example.backend.repository;

import com.example.backend.constant.SeatStatus;
import com.example.backend.dto.response.CinemaHallResponseDTO;
import com.example.backend.model.CinemaHall;
import com.example.backend.model.Seat;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {
    @Query(value = """
                    SELECT ch
                    FROM CinemaHall AS ch
                        LEFT JOIN FETCH ch.seats s
                        LEFT JOIN FETCH ch.movie m
            """)
    List<CinemaHall> getAll();

    @Modifying
    @Transactional
    @Query(value = "UPDATE cinema_hall ch SET movie_id = :#{#cinemaHall.movie.id}, movie_session = :#{#cinemaHall.movieSession} WHERE id = :#{#cinemaHall.id}", nativeQuery = true)
    int update(CinemaHall cinemaHall);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cinema_hall ch WHERE ch.id = :id", nativeQuery = true)
    int delete(Long id);

    Optional<CinemaHall> findByMovieIdAndMovieSession(Long movieId, Date movieSession);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO seat (seat_number, status, cinema_hall_id, price) VALUES (:#{#seat.seatNumber} ,:#{#seat.status} , :#{#seat.cinemaHallId} ,:#{#seat.price})", nativeQuery = true)
    void insertSeats(Seat seat);
}
