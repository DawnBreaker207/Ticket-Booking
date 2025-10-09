package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.TheaterRequestDTO;
import com.example.backend.dto.response.TheaterResponseDTO;
import com.example.backend.service.TheaterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theater")
@Tag(name = "Theater", description = "Operations related to theater")
@RequiredArgsConstructor
@Slf4j
public class TheaterController {

    private final TheaterService theaterService;

    @GetMapping("")
    public ResponseObject<List<TheaterResponseDTO>> findAll() {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseObject<TheaterResponseDTO> findOne(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.findOne(id));
    }

    @GetMapping("/search")
    public ResponseObject<List<TheaterResponseDTO>> searchTheaterByLocation(@RequestParam(required = false) String location) {
        log.info("Search theater by location {}", location);
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.findByLocation(location));
    }

    @PostMapping("")
    public ResponseObject<TheaterResponseDTO> create(@RequestBody TheaterRequestDTO theater) {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.create(theater));
    }

    @PutMapping("/{id}")
    public ResponseObject<TheaterResponseDTO> update(@PathVariable Long id, @RequestBody TheaterRequestDTO theater) {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.update(id, theater));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        theaterService.remove(id);
    }

}
