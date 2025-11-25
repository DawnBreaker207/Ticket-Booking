package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.TheaterRequestDTO;
import com.dawn.backend.dto.response.TheaterResponseDTO;
import org.springframework.data.domain.Pageable;

public interface TheaterService {

    ResponsePage<TheaterResponseDTO> findAll(Pageable pageable);

    ResponsePage<TheaterResponseDTO> findByLocation(String location, Pageable pageable);

    TheaterResponseDTO findOne(Long id);

    TheaterResponseDTO create(TheaterRequestDTO theater);

    TheaterResponseDTO update(Long id, TheaterRequestDTO theaterDetails);

    void remove(Long id);
}
