package com.dawn.backend.service.Impl;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.constant.Message;
import com.dawn.backend.constant.SeatStatus;
import com.dawn.backend.dto.request.ShowtimeFilterRequest;
import com.dawn.backend.dto.request.ShowtimeRequest;
import com.dawn.backend.dto.response.ShowtimeResponse;
import com.dawn.backend.exception.wrapper.MovieNotFoundException;
import com.dawn.backend.exception.wrapper.ShowtimeNotFoundException;
import com.dawn.backend.exception.wrapper.TheaterNotFoundException;
import com.dawn.backend.helper.ShowtimeMappingHelper;
import com.dawn.backend.model.Movie;
import com.dawn.backend.model.Seat;
import com.dawn.backend.model.Showtime;
import com.dawn.backend.model.Theater;
import com.dawn.backend.repository.MovieRepository;
import com.dawn.backend.repository.SeatRepository;
import com.dawn.backend.repository.ShowtimeRepository;
import com.dawn.backend.repository.TheaterRepository;
import com.dawn.backend.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowtimeServiceImpl implements ShowtimeService {
    public static final String SHOWTIME_CACHE = "showtime";

    private final ShowtimeRepository showtimeRepository;

    private final MovieRepository movieRepository;

    private final TheaterRepository theaterRepository;

    private final SeatRepository seatRepository;

    @Override
    public List<ShowtimeResponse> getByDate(LocalDate date) {
        log.info("Fetching showtime for date: {}", date);
        return showtimeRepository
                .findByShowDate(date)
                .stream()
                .map(ShowtimeMappingHelper::map)
                .toList();
    }

    @Override
    public List<ShowtimeResponse> getByMovie(Long movieId) {
        log.info("Fetching showtime for movie id: {}", movieId);
        Movie movie = movieRepository
                .findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(Message.Exception.MOVIE_NOT_FOUND));
        return showtimeRepository
                .findByMovie(movie)
                .stream()
                .map(ShowtimeMappingHelper::map)
                .toList();
    }

    @Override
    public ResponsePage<ShowtimeResponse> getByTheater(ShowtimeFilterRequest req, Pageable pageable) {
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : LocalDate.now();
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : start.plusDays(30);
        log.info("Fetching showtime for theater id: {}", req.getTheaterId());
        Theater theater = theaterRepository
                .findById(req.getTheaterId())
                .orElseThrow(() -> new TheaterNotFoundException(Message.Exception.THEATER_NOT_FOUND));
        return ResponsePage.of(showtimeRepository
                .findByTheater(theater, start, end, pageable)
                .map(ShowtimeMappingHelper::map));
    }

    @Override
    public List<ShowtimeResponse> getAvailableShowtime(LocalDate date) {
        log.info("Fetching available showtime from date: {}", date);
        return showtimeRepository
                .findAvailableShowtimeFromDate(date)
                .stream()
                .map(ShowtimeMappingHelper::map)
                .toList();
    }

    @Override
    public List<ShowtimeResponse> getAvailableShowtimeForMovie(Long movieId, LocalDate date) {
        log.info("Fetching available showtime for movie id: {} form date {}", movieId, date);
        return showtimeRepository
                .findAvailableShowtimeForMovieFromDate(movieId, date)
                .stream()
                .map(ShowtimeMappingHelper::map)
                .toList();
    }

    @Override
    public ShowtimeResponse getById(Long id) {
        log.info("Fetching showtime with id {}", id);
        return showtimeRepository
                .findById(id)
                .map(ShowtimeMappingHelper::map)
                .orElseThrow(() -> new ShowtimeNotFoundException(Message.Exception.SHOWTIME_NOT_FOUND));
    }

    @Override
    @Transactional
    public ShowtimeResponse add(ShowtimeRequest showtimeRequest) {
        log.info("Adding new showtime for movie id: {} at theater id: {}", showtimeRequest.getMovieId(), showtimeRequest.getTheaterId());

        Movie movie = movieRepository
                .findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException(Message.Exception.MOVIE_NOT_FOUND));

        Theater theater = theaterRepository
                .findById(showtimeRequest.getTheaterId())
                .orElseThrow(() -> new TheaterNotFoundException(Message.Exception.THEATER_NOT_FOUND));

        if (showtimeRequest.getTotalSeats() > theater.getCapacity()) {
            throw new IllegalArgumentException("Total seats cannot be greater than capacity of " + theater.getCapacity());
        }

        //        All seat available at first
        Showtime showtime = Showtime
                .builder()
                .movie(movie)
                .theater(theater)
                .showDate(showtimeRequest.getShowDate())
                .showTime(showtimeRequest.getShowTime())
                .totalSeats(showtimeRequest.getTotalSeats())
                .availableSeats(showtimeRequest.getTotalSeats())
                .price(showtimeRequest.getPrice())
                .build();


        Showtime savedShowtime = showtimeRepository.save(showtime);
        log.info("Saved showtime with ID: {}", savedShowtime.getPrice());

//        Create and saved seats
        List<Seat> seats = createSeats(savedShowtime);
        seatRepository.saveAll(seats);


        log.info("Created {} seats for showtime ID: {}", seats.size(), savedShowtime.getId());
        return ShowtimeMappingHelper.map(savedShowtime);
    }

    @Override
    @Transactional
    @Cacheable(value = SHOWTIME_CACHE, key = "'id:' + #id")
    public ShowtimeResponse update(Long id, ShowtimeRequest showtimeDetails) {
        log.info("Updating showtime with id: {}", id);
        Showtime showtime = showtimeRepository
                .findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(Message.Exception.THEATER_NOT_FOUND));

        if (showtimeDetails.getMovieId() != null) {
            Movie movie = movieRepository
                    .findById(showtimeDetails.getMovieId())
                    .orElseThrow(() -> new MovieNotFoundException(Message.Exception.MOVIE_NOT_FOUND));
            showtime.setMovie(movie);
        }

        if (showtimeDetails.getTheaterId() != null) {
            Theater theater = theaterRepository
                    .findById(showtimeDetails.getTheaterId())
                    .orElseThrow(() -> new TheaterNotFoundException(Message.Exception.THEATER_NOT_FOUND));
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
    @CacheEvict(value = SHOWTIME_CACHE, key = "'id:' + #id")
    public void delete(Long id) {
        log.info("Deleting showtime with id: {}", id);
        Showtime showtime = showtimeRepository
                .findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(Message.Exception.SHOWTIME_NOT_FOUND));

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


    private List<Seat> createSeats(Showtime showtime) {
        List<Seat> seats = new ArrayList<>();
        int totalSeats = showtime.getTotalSeats();
        int seatPerRow = 10;

        for (int i = 1; i <= totalSeats; i++) {
            char row = (char) ('A' + (i - 1) / seatPerRow);
            int seatNum = (i - 1) % seatPerRow + 1;

            Seat seat = Seat
                    .builder()
                    .showtime(showtime)
                    .seatNumber(String.format("%c%d", row, seatNum))
                    .status(SeatStatus.AVAILABLE)
                    .build();

            seats.add(seat);
        }

        return seats;
    }

}
