package com.dawn.cinema.service;

import com.dawn.cinema.dto.request.TheaterRequest;
import com.dawn.cinema.dto.response.TheaterResponse;
import com.dawn.common.core.dto.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface TheaterService {

    ResponsePage<TheaterResponse> findAll(Pageable pageable);

    ResponsePage<TheaterResponse> findByLocation(String location, Pageable pageable);

    TheaterResponse findOne(Long id);

    TheaterResponse create(TheaterRequest theater);

    TheaterResponse update(Long id, TheaterRequest theaterDetails);

    void remove(Long id);
}
