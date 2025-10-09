package com.example.backend.helper;

import com.example.backend.dto.request.ShowtimeRequestDTO;
import com.example.backend.dto.response.ShowtimeResponseDTO;
import com.example.backend.model.Showtime;

public interface ShowtimeMappingHelper {
    static Showtime map(final ShowtimeRequestDTO showtime) {
        return Showtime
                .builder()
                .showDate(showtime.getShowDate())
                .showTime(showtime.getShowTime())
                .price(showtime.getPrice())
                .totalSeats(showtime.getTotalSeats())
                .build();
    }

    static ShowtimeResponseDTO map(final Showtime showtime) {
        return ShowtimeResponseDTO
                .builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .movieTitle(showtime.getMovie().getTitle())
                .moviePosterUrl(showtime.getMovie().getPoster())
                .theaterId(showtime.getTheater().getId())
                .theaterName(showtime.getTheater().getName())
                .theaterLocation(showtime.getTheater().getLocation())
                .showDate(showtime.getShowDate())
                .showTime(showtime.getShowTime())
                .price(showtime.getPrice())
                .totalSeats(showtime.getTotalSeats())
                .availableSeats(showtime.getAvailableSeats())
                .createdAt(showtime.getCreatedAt())
                .updatedAt(showtime.getUpdatedAt())
                .build();
    }
}
