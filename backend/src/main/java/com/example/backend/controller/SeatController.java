package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.response.SeatResponseDTO;
import com.example.backend.service.SeatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@Tag(name = "Seats", description = "Operations related to seat")
@RequiredArgsConstructor
@Slf4j
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/showtime/{showtimeId}")
    public ResponseObject<List<SeatResponseDTO>> getByShowtime(@PathVariable Long showtimeId) {
        log.info("Fetching seats for showtime id: {}", showtimeId);
        return new ResponseObject<>(HttpStatus.OK, "Success", seatService.getByShowtime(showtimeId));
    }

    @GetMapping("/showtime/{showtimeId}/available")
    public ResponseObject<List<SeatResponseDTO>> getAvailableSeatsByShowtime(@PathVariable Long showtimeId) {
        log.info("Fetching available seats for showtime id: {}", showtimeId);
        return new ResponseObject<>(HttpStatus.OK, "Success", seatService.getAvailableSeatByShowtime(showtimeId));
    }

    @PostMapping("/showtime/{showtimeId}/create")
    public ResponseObject<List<SeatResponseDTO>> createSeatsForShowtime(@PathVariable Long showtimeId) {
        log.info("Manually creating seats for showtime id: {}", showtimeId);
        return new ResponseObject<>(HttpStatus.OK, "Success", seatService.getAvailableSeatByShowtime(showtimeId));
    }
}
