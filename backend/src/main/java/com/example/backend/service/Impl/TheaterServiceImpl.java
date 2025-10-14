package com.example.backend.service.Impl;

import com.example.backend.constant.Message;
import com.example.backend.dto.request.TheaterRequestDTO;
import com.example.backend.dto.response.TheaterResponseDTO;
import com.example.backend.exception.wrapper.TheaterNotFoundException;
import com.example.backend.helper.TheaterMappingHelper;
import com.example.backend.model.Theater;
import com.example.backend.repository.TheaterRepository;
import com.example.backend.service.TheaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;

    @Override
    public List<TheaterResponseDTO> findAll() {
        return theaterRepository
                .findAll()
                .stream()
                .map(TheaterMappingHelper::map)
                .toList();
    }

    @Override
    public TheaterResponseDTO findOne(Long id) {
        return theaterRepository
                .findById(id)
                .map(TheaterMappingHelper::map)
                .orElseThrow(() -> new TheaterNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.THEATER_NOT_FOUND));
    }

    @Override
    public List<TheaterResponseDTO> findByLocation(String location) {
        log.info("Search theater by location {}", location);
        return theaterRepository
                .findByLocationContainingIgnoreCase(location)
                .stream()
                .map(TheaterMappingHelper::map)
                .toList();
    }

    @Override
    @Transactional
    public TheaterResponseDTO create(TheaterRequestDTO theater) {
        log.info("Add new theater: {}", theater);
        return TheaterMappingHelper.map(theaterRepository.save(TheaterMappingHelper.map(theater)));

    }

    @Override
    @Transactional
    public TheaterResponseDTO update(Long id, TheaterRequestDTO theaterDetails) {
        Theater theater = theaterRepository
                .findById(id)
                .orElseThrow(() -> new TheaterNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.THEATER_NOT_FOUND));
        theater.setName(theaterDetails.getName());
        theater.setLocation(theaterDetails.getLocation());
        theater.setCapacity(theaterDetails.getCapacity());
        theater.markUpdated();
        return TheaterMappingHelper.map(theaterRepository.save(theater));
    }

    @Override
    public void remove(Long id) {
        theaterRepository
                .findById(id)
                .orElseThrow(() -> new TheaterNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.THEATER_NOT_FOUND));
        theaterRepository.deleteById(id);
    }


}
