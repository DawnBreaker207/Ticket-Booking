package com.dawn.backend.helper;

import com.dawn.backend.dto.request.ShowtimeRequest;
import com.dawn.backend.dto.response.ShowtimeResponse;
import com.dawn.backend.model.Showtime;

public interface ShowtimeMappingHelper {
    static Showtime map(final ShowtimeRequest showtime) {
        return Showtime
                .builder()
                .showDate(showtime.getShowDate())
                .showTime(showtime.getShowTime())
                .price(showtime.getPrice())
                .totalSeats(showtime.getTotalSeats())
                .build();
    }

    static ShowtimeResponse map(final Showtime showtime) {
        return ShowtimeResponse
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
