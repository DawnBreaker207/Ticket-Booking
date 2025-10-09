package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.TheaterRequestDTO;
import com.example.backend.dto.response.TheaterResponseDTO;
import com.example.backend.service.TheaterService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get all theaters", description = "Return a list of all theaters")
    public ResponseObject<List<TheaterResponseDTO>> findAll() {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get theater by ID", description = "Return theater by its ID")
    public ResponseObject<TheaterResponseDTO> findOne(@PathVariable Long id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.findOne(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search theaters by location", description = "Return a theaters matching the location search term")
    public ResponseObject<List<TheaterResponseDTO>> searchTheaterByLocation(@RequestParam(required = false) String location) {
        log.info("Search theater by location {}", location);
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.findByLocation(location));
    }

    @PostMapping("")
    @Operation(summary = "Add a new theater", description = "Create a new theater (Admin only)")
    public ResponseObject<TheaterResponseDTO> create(@RequestBody TheaterRequestDTO theater) {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.create(theater));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a theater", description = "Updates an existing theater (Admin only)")
    public ResponseObject<TheaterResponseDTO> update(@PathVariable Long id, @RequestBody TheaterRequestDTO theater) {
        return new ResponseObject<>(HttpStatus.OK, "Success", theaterService.update(id, theater));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a theater", description = "Delete an existing theater (Admin only)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        theaterService.remove(id);
    }

}
