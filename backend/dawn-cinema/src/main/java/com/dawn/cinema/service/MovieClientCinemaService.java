package com.dawn.cinema.service;


import com.dawn.cinema.dto.response.MovieDTO;

public interface MovieClientCinemaService {
    MovieDTO findOne(Long id);
}
