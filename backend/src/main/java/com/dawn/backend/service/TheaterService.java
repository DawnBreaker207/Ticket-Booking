package com.dawn.backend.service;

import com.dawn.backend.dto.request.TheaterRequestDTO;
import com.dawn.backend.dto.response.TheaterResponseDTO;

import java.util.List;

public interface TheaterService {

    List<TheaterResponseDTO> findAll();

    TheaterResponseDTO findOne(Long id);

    List<TheaterResponseDTO> findByLocation(String location);

    TheaterResponseDTO create(TheaterRequestDTO theater);

    TheaterResponseDTO update(Long id, TheaterRequestDTO theaterDetails);

    void remove(Long id);

}
