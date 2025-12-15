package com.dawn.booking.service;


import com.dawn.booking.dto.response.MovieDTO;

public interface MovieClientBookingService {
    MovieDTO findOne(Long id);
}
