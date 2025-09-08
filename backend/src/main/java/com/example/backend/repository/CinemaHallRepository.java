package com.example.backend.repository;

import com.example.backend.constant.SeatStatus;
import com.example.backend.model.CinemaHall;
import com.example.backend.model.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CinemaHallRepository extends DAO<CinemaHall, Long> {
    @Override
    List<CinemaHall> findAll();

    @Override
    Optional<CinemaHall> findById(Long aLong);

    @Override
    int insert(CinemaHall cinemaHall);

    @Override
    int update(CinemaHall cinemaHall);

    default CinemaHall save(CinemaHall input) {
        if (input.getId() == null) {
            insert(input);
        } else {
            update(input);
        }
        return input;
    }


    @Override
    void delete(Long aLong);

    Optional<CinemaHall> findByMovieIdAndMovieSession(@Param("movieId") Long movieId, @Param("movieSession") String movieSession);

    void updateSeatStatus(@Param("seatIds") List<String> seatIds, @Param("seatStatus") SeatStatus status);

    void insertSeats(@Param("list") List<Seat> seat);
}
