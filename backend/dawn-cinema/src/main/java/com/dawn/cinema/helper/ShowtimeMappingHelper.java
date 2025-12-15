package com.dawn.cinema.helper;

import com.dawn.cinema.dto.request.ShowtimeRequest;
import com.dawn.cinema.dto.response.MovieDTO;
import com.dawn.cinema.dto.response.ShowtimeResponse;
import com.dawn.cinema.model.Showtime;

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

    static ShowtimeResponse map(final Showtime showtime, MovieDTO movie) {
        return ShowtimeResponse
                .builder()
                .id(showtime.getId())
                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .moviePosterUrl(movie.getPoster())
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
