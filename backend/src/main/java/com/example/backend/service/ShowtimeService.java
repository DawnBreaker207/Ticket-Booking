package com.example.backend.service;

import com.example.backend.dto.request.ShowtimeRequestDTO;
import com.example.backend.dto.response.ShowtimeResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeService {


    List<ShowtimeResponseDTO> getByDate(LocalDate date);

    List<ShowtimeResponseDTO> getByMovie(Long movieId);

    List<ShowtimeResponseDTO> getByTheater(Long theaterId);

    List<ShowtimeResponseDTO> getAvailableShowtime(LocalDate date);

    // get available showtimes for a specific movie from a date (paginated)
    // this is what the booking page uses
    List<ShowtimeResponseDTO> getAvailableShowtimeForMovie(Long movieId, LocalDate date);

    ShowtimeResponseDTO getById(Long id);

    ShowtimeResponseDTO add(ShowtimeRequestDTO showtime);

    ShowtimeResponseDTO update(Long id, ShowtimeRequestDTO showtimeDetails);

    void delete(Long id);

}
