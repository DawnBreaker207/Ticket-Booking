package com.dawn.cinema.service.Impl;

import com.dawn.cinema.dto.request.TheaterRequest;
import com.dawn.cinema.dto.response.TheaterResponse;
import com.dawn.cinema.helper.TheaterMappingHelper;
import com.dawn.cinema.model.Theater;
import com.dawn.cinema.repository.TheaterRepository;
import com.dawn.cinema.service.TheaterService;
import com.dawn.common.config.response.ResponsePage;
import com.dawn.common.constant.Message;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TheaterServiceImpl implements TheaterService {
    public static final String THEATER_CACHE = "theater";
    private final TheaterRepository theaterRepository;

    @Override
//    @Cacheable(value = THEATER_CACHE)
    public ResponsePage<TheaterResponse> findAll(Pageable pageable) {
        return ResponsePage.of(
                theaterRepository
                        .findAll(pageable)
                        .map(TheaterMappingHelper::map));
    }

    @Override
//    @Cacheable(value = THEATER_CACHE, key = "'location:' + #location")
    public ResponsePage<TheaterResponse> findByLocation(String location, Pageable pageable) {
        log.info("Search theater by location {}", location);
        return ResponsePage.of(theaterRepository
                .findByLocationContainingIgnoreCase(location, pageable)
                .map(TheaterMappingHelper::map)
        );
    }

    @Override
//    @Cacheable(value = THEATER_CACHE, key = "'id:' + #id")
    public TheaterResponse findOne(Long id) {
        return theaterRepository
                .findById(id)
                .map(TheaterMappingHelper::map)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.THEATER_NOT_FOUND));
    }

    @Override
    @Transactional
    @CachePut(value = THEATER_CACHE, key = "'id:' + #result.id")
    public TheaterResponse create(TheaterRequest theater) {
        log.info("Add new theater: {}", theater);
        return TheaterMappingHelper.map(theaterRepository.save(TheaterMappingHelper.map(theater)));

    }

    @Override
    @Transactional
    @CachePut(value = THEATER_CACHE, key = "'id:' + #id")
    public TheaterResponse update(Long id, TheaterRequest theaterDetails) {
        Theater theater = theaterRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.THEATER_NOT_FOUND));
        theater.setName(theaterDetails.getName());
        theater.setLocation(theaterDetails.getLocation());
        theater.setCapacity(theaterDetails.getCapacity());
        return TheaterMappingHelper.map(theaterRepository.save(theater));
    }

    @Override
    @CacheEvict(value = THEATER_CACHE, key = "'id:' + #id")
    public void remove(Long id) {
        theaterRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.THEATER_NOT_FOUND));
        theaterRepository.deleteById(id);
    }


}
