package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.TheaterRequest;
import com.dawn.backend.dto.response.TheaterResponse;
import org.springframework.data.domain.Pageable;

public interface TheaterService {

    ResponsePage<TheaterResponse> findAll(Pageable pageable);

    ResponsePage<TheaterResponse> findByLocation(String location, Pageable pageable);

    TheaterResponse findOne(Long id);

    TheaterResponse create(TheaterRequest theater);

    TheaterResponse update(Long id, TheaterRequest theaterDetails);

    void remove(Long id);
}
