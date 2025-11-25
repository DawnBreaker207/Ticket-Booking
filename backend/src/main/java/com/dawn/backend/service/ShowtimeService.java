package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.ShowtimeRequestDTO;
import com.dawn.backend.dto.response.ShowtimeResponseDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeService {


    List<ShowtimeResponseDTO> getByDate(LocalDate date);

    List<ShowtimeResponseDTO> getByMovie(Long movieId);

    ResponsePage<ShowtimeResponseDTO> getByTheater(Long theaterId, Pageable pageable);

    List<ShowtimeResponseDTO> getAvailableShowtime(LocalDate date);

    // get available showtimes for a specific movie from a date (paginated)
    // this is what the booking page uses
    List<ShowtimeResponseDTO> getAvailableShowtimeForMovie(Long movieId, LocalDate date);

    ShowtimeResponseDTO getById(Long id);

    ShowtimeResponseDTO add(ShowtimeRequestDTO showtime);

    ShowtimeResponseDTO update(Long id, ShowtimeRequestDTO showtimeDetails);

    void delete(Long id);

}
