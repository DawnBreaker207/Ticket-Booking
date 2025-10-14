package com.example.backend.service;

import com.example.backend.dto.request.TheaterRequestDTO;
import com.example.backend.dto.response.TheaterResponseDTO;

import java.util.List;

public interface TheaterService {

    List<TheaterResponseDTO> findAll();

    TheaterResponseDTO findOne(Long id);

    List<TheaterResponseDTO> findByLocation(String location);

    TheaterResponseDTO create(TheaterRequestDTO theater);

    TheaterResponseDTO update(Long id, TheaterRequestDTO theaterDetails);

    void remove(Long id);

}
