package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.ShowtimeFilterRequest;
import com.dawn.backend.dto.request.ShowtimeRequest;
import com.dawn.backend.dto.response.ShowtimeResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeService {


    List<ShowtimeResponse> getByDate(LocalDate date);

    List<ShowtimeResponse> getByMovie(Long movieId);

    ResponsePage<ShowtimeResponse> getByTheater(ShowtimeFilterRequest req, Pageable pageable);

    List<ShowtimeResponse> getAvailableShowtime(LocalDate date);

    // get available showtimes for a specific movie from a date (paginated)
    // this is what the booking page uses
    List<ShowtimeResponse> getAvailableShowtimeForMovie(Long movieId, LocalDate date);

    ShowtimeResponse getById(Long id);

    ShowtimeResponse add(ShowtimeRequest showtime);

    ShowtimeResponse update(Long id, ShowtimeRequest showtimeDetails);

    void delete(Long id);

}
