package com.example.backend.service.Impl;

import com.example.backend.dto.request.ShowtimeRequestDTO;
import com.example.backend.dto.response.ShowtimeResponseDTO;
import com.example.backend.exception.wrapper.CinemaHallNotFoundException;
import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.helper.ShowtimeMappingHelper;
import com.example.backend.model.Movie;
import com.example.backend.model.Seat;
import com.example.backend.model.Showtime;
import com.example.backend.model.Theater;
import com.example.backend.repository.MovieRepository;
import com.example.backend.repository.SeatRepository;
import com.example.backend.repository.ShowtimeRepository;
import com.example.backend.repository.TheaterRepository;
import com.example.backend.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    private final MovieRepository movieRepository;

    private final TheaterRepository theaterRepository;

    private final SeatRepository seatRepository;

    @Override
    public List<ShowtimeResponseDTO> getByDate(LocalDate date) {
        log.info("Fetching showtime for date: {}", date);
        return showtimeRepository.findByShowDate(date).stream().map(ShowtimeMappingHelper::map).toList();
    }

    @Override
    public List<ShowtimeResponseDTO> getByMovie(Long movieId) {
        log.info("Fetching showtime for movie id: {}", movieId);
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Theater not found with id: " + movieId));
        return showtimeRepository.findByMovie(movie).stream().map(ShowtimeMappingHelper::map).toList();
    }

    @Override
    public List<ShowtimeResponseDTO> getByTheater(Long theaterId) {
        log.info("Fetching showtime for theater id: {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new CinemaHallNotFoundException("Theater not found with id: " + theaterId));
        return showtimeRepository.findByTheater(theater).stream().map(ShowtimeMappingHelper::map).toList();
    }

    @Override
    public List<ShowtimeResponseDTO> getAvailableShowtime(LocalDate date) {
        log.info("Fetching available showtime from date: {}", date);
        return showtimeRepository.findAvailableShowtimeFromDate(date).stream().map(ShowtimeMappingHelper::map).toList();
    }

    @Override
    public List<ShowtimeResponseDTO> getAvailableShowtimeForMovie(Long movieId, LocalDate date) {
        log.info("Fetching available showtime for movie id: {} form date {}", movieId, date);
        return showtimeRepository.findAvailableShowtimeForMovieFromDate(movieId, date).stream().map(ShowtimeMappingHelper::map).toList();
    }

    @Override
    public ShowtimeResponseDTO getById(Long id) {
        log.info("Fetching showtime with id {}", id);
        return showtimeRepository.findById(id).map(ShowtimeMappingHelper::map).orElseThrow(() -> new CinemaHallNotFoundException("Showtime not found with id: " + id));
    }

    @Override
    @Transactional
    public ShowtimeResponseDTO add(ShowtimeRequestDTO showtimeRequest) {
        log.info("Adding new showtime for movie id: {} at theater id: {}", showtimeRequest.getMovieId(), showtimeRequest.getTheaterId());

        Movie movie = movieRepository.findById(showtimeRequest.getMovieId()).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + showtimeRequest.getMovieId()));

        Theater theater = theaterRepository.findById(showtimeRequest.getTheaterId()).orElseThrow(() -> new CinemaHallNotFoundException("Theater not found with id: " + showtimeRequest.getTheaterId()));

        Showtime showtime = Showtime
                .builder()
                .movie(movie)
                .theater(theater)
                .showDate(showtimeRequest.getShowDate())
                .showTime(showtimeRequest.getShowTime())
                .totalSeats(showtimeRequest.getTotalSeats())
                .price(showtimeRequest.getPrice())
                .build();

        if (showtime.getMovie() == null || showtime.getMovie().getId() == null) {
            throw new IllegalArgumentException("Movie is required for showtime");
        }

        if (showtime.getTheater() == null || showtime.getTheater().getId() == null) {
            throw new IllegalArgumentException("Theater is required for showtime");
        }

        showtime.setMovie(movie);
        showtime.setTheater(theater);


        if (showtime.getTotalSeats() > theater.getCapacity()) {
            throw new IllegalArgumentException("Total seats cannot be greater than capacity of " + theater.getCapacity());
        }

//        All seat available at first
        showtime.setAvailableSeats(showtime.getTotalSeats());
        Showtime savedShowtime = showtimeRepository.save(showtime);

        log.info("Saved showtime with ID: {}", savedShowtime.getPrice());

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= showtime.getTotalSeats(); i++) {
            Seat seat = new Seat();
            seat.setShowtime(savedShowtime);
            seat.setSeatNumber(String.format("%c%d", ((i - 1) / 10), ((i - 1) % 10) + 1));
            seats.add(seat);
        }

        seatRepository.saveAll(seats);


        log.info("Created {} seats for showtime ID: {}", seats.size(), savedShowtime.getId());
        return ShowtimeMappingHelper.map(savedShowtime);

    }

    @Override
    @Transactional
    public ShowtimeResponseDTO update(Long id, ShowtimeRequestDTO showtimeDetails) {
        log.info("Updating showtime with id: {}", id);
        Showtime showtime = showtimeRepository.findById(id).
                orElseThrow(() -> new CinemaHallNotFoundException("Showtime not found with id: " + id));

        if (showtimeDetails.getMovieId() != null) {
            Movie movie = movieRepository.findById(showtimeDetails.getMovieId()).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + showtimeDetails.getMovieId()));
            showtime.setMovie(movie);
        }

        if (showtimeDetails.getTheaterId() != null) {
            Theater theater = theaterRepository.findById(showtimeDetails.getTheaterId())
                    .orElseThrow(() -> new CinemaHallNotFoundException("Theater not found with id: " + showtimeDetails.getTheaterId()));
            showtime.setTheater(theater);
        }

        if (showtimeDetails.getShowDate() != null) {
            showtime.setShowDate(showtimeDetails.getShowDate());
        }

        if (showtimeDetails.getShowTime() != null) {
            showtime.setShowTime(showtimeDetails.getShowTime());
        }

        if (showtimeDetails.getPrice() != null) {
            showtime.setPrice(showtimeDetails.getPrice());
        }


//        DANGER: Don't update total seats or available seats directly - would break reservation!
        Showtime updatedShowtime = showtimeRepository.save(showtime);
        log.info("Updated showtime with ID: {}", updatedShowtime.getId());
        return ShowtimeMappingHelper.map(updatedShowtime);


    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting showtime with id: {}", id);
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new CinemaHallNotFoundException("Showtime not found with id: " + id));

//     Safety check - can't delete if people have tickets
        if (showtime.getReservations() != null && !showtime.getReservations().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete showtime with existing reservations");
        }


//        Delete the seats first (FK constraint)
        List<Seat> seats = seatRepository.findByShowtime(showtime);

        if (seats != null && !seats.isEmpty()) {
            log.info("Deleting {} seats for showtime ID: {}", seats.size(), id);

            seatRepository.deleteAll(seats);
        }

        showtimeRepository.delete(showtime);
        log.info("Deleted showtime with ID: {}", id);
    }


//    private void insertSeats(List<Integer> input) {
//        List<Seat> seats = new ArrayList<>();
//        for (char row = 'A'; row <= 'E'; row++) {
//            for (int i = 1; i <= 10; i++) {
//                Seat seat = new Seat();
//                seat.setSeatNumber(row + String.valueOf(i));
//                seat.setCinemaHall(cinemaHall);
//                seat.setStatus(SeatStatus.AVAILABLE);
//                seat.setPrice(BigDecimal.valueOf(50000));
//                seats.add(seat);
//            }
//        }
//        seats.forEach(theaterRepository::insertSeats);
//    }

}
