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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TheaterServiceImpl implements TheaterService {
    public static final String THEATER_CACHE = "theater";
    private final TheaterRepository theaterRepository;

    @Override
//    @Cacheable(value = THEATER_CACHE)
    public List<TheaterResponseDTO> findAll() {
        return theaterRepository
                .findAll()
                .stream()
                .map(TheaterMappingHelper::map)
                .toList();
    }

    @Override
//    @Cacheable(value = THEATER_CACHE, key = "'id:' + #id")
    public TheaterResponseDTO findOne(Long id) {
        return theaterRepository
                .findById(id)
                .map(TheaterMappingHelper::map)
                .orElseThrow(() -> new TheaterNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.THEATER_NOT_FOUND));
    }

    @Override
//    @Cacheable(value = THEATER_CACHE, key = "'location:' + #location")
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
    @CachePut(value = THEATER_CACHE, key = "'id:' + #result.id")
    public TheaterResponseDTO create(TheaterRequestDTO theater) {
        log.info("Add new theater: {}", theater);
        return TheaterMappingHelper.map(theaterRepository.save(TheaterMappingHelper.map(theater)));

    }

    @Override
    @Transactional
    @CachePut(value = THEATER_CACHE, key = "'id:' + #id")
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
    @CacheEvict(value = THEATER_CACHE, key = "'id:' + #id")
    public void remove(Long id) {
        theaterRepository
                .findById(id)
                .orElseThrow(() -> new TheaterNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.THEATER_NOT_FOUND));
        theaterRepository.deleteById(id);
    }


}
