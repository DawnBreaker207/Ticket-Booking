package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.ShowtimeRequestDTO;
import com.example.backend.dto.response.ShowtimeResponseDTO;
import com.example.backend.model.Showtime;
import com.example.backend.service.ShowtimeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/showtime")
@Tag(name = "Showtime", description = "Operations related to showtime")
@RequiredArgsConstructor
@Slf4j
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @GetMapping()
    public ResponseObject<List<ShowtimeResponseDTO>> getShowtimeByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        log.info("Fetching showtime for date: {}", date);
        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.getByDate(date));
    }


    @GetMapping("/movies/{movieId}")
    public ResponseObject<List<ShowtimeResponseDTO>> getShowtimeByMovie(@PathVariable Long movieId) {
        log.info("Fetching showtime for movie id: {}", movieId);

        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.getByMovie(movieId));
    }

    @GetMapping("/theaters/{theaterId}")
    public ResponseObject<List<ShowtimeResponseDTO>> getShowtimeByTheater(@PathVariable Long theaterId) {
        log.info("Fetching showtime for theater id: {}", theaterId);

        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.getByTheater(theaterId));
    }

    @GetMapping("/available")
    public ResponseObject<List<ShowtimeResponseDTO>> getAvailableShowtime(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate searchDate = date != null ? date : LocalDate.now();
        log.info("Fetching available showtime from date: {}", searchDate);
        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.getAvailableShowtime(searchDate));
    }

    @GetMapping("/available/movies/{movieId}")
    public ResponseObject<List<ShowtimeResponseDTO>> getAvailableShowtimeForMovie(@PathVariable Long movieId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate searchDate = date != null ? date : LocalDate.now();
        log.info("Fetching available showtime from movie id: {} from date {}", movieId, searchDate);
        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.getAvailableShowtimeForMovie(movieId, searchDate));
    }

    @GetMapping("/{id}")
    public ResponseObject<ShowtimeResponseDTO> getShowtimeById(@PathVariable Long id) {
        log.info("Fetching showtime with id: {}", id);
        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.getById(id));
    }

    @PostMapping()
    public ResponseObject<ShowtimeResponseDTO> add(@Valid @RequestBody ShowtimeRequestDTO showtimeRequest) {
        log.info("Create showtime: {}", showtimeRequest);
        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.add(showtimeRequest));
    }

    @PutMapping("/{id}")
    public ResponseObject<ShowtimeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ShowtimeRequestDTO showtimeDetails) {
        log.info("Updating showtime with id: {}", id);
        return new ResponseObject<>(HttpStatus.OK, "Success", showtimeService.update(id, showtimeDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Long id) {
        log.info("Deleting showtime with id: {}", id);
        showtimeService.delete(id);
        return new ResponseObject<>(HttpStatus.NO_CONTENT, "Success", null);
    }
}
