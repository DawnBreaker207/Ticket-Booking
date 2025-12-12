package com.dawn.api.catalog.service;


import com.dawn.api.catalog.dto.MovieDTO;

public interface MovieClientService {
    MovieDTO findOne(Long id);
}
