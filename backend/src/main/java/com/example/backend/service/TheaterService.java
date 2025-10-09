package com.example.backend.service;

import com.example.backend.dto.request.TheaterRequestDTO;
import com.example.backend.dto.response.TheaterResponseDTO;
import com.example.backend.model.Theater;

import java.util.List;

public interface TheaterService {

    List<TheaterResponseDTO> findAll();

    TheaterResponseDTO findOne(Long id);

    List<TheaterResponseDTO> findByLocation(String location);

    TheaterResponseDTO create(TheaterRequestDTO cinemaHall);

    TheaterResponseDTO update(Long id, TheaterRequestDTO cinemaHall);

    void remove(Long id);

}
