package com.dawn.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserReservationResponse {
    private String reservationId;

    private String moviePoster;

    private String movieTitle;

    private String showtime;

    private LocalDate date;

    private LocalTime time;

    private String theater;

    private List<String> seats;

    private Integer amount;
}
