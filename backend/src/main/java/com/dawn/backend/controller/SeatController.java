package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.dto.response.SeatResponse;
import com.dawn.backend.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get all seats for a showtime", description = "Returns all seats for a specific showtime")
    public ResponseObject<List<SeatResponse>> getByShowtime(@PathVariable Long showtimeId) {
        log.info("Fetching seats for showtime id: {}", showtimeId);
        return new ResponseObject<>(HttpStatus.OK, "Success", seatService.getByShowtime(showtimeId));
    }

    @GetMapping("/showtime/{showtimeId}/available")
    @Operation(summary = "Get available seats for a showtime", description = "Returns available seats for a specific showtime")
    public ResponseObject<List<SeatResponse>> getAvailableSeatsByShowtime(@PathVariable Long showtimeId) {
        log.info("Fetching available seats for showtime id: {}", showtimeId);
        return new ResponseObject<>(HttpStatus.OK, "Success", seatService.getAvailableSeatByShowtime(showtimeId));
    }

    @PostMapping("/showtime/{showtimeId}/create")
    @Operation(summary = "Create seat for a showtime", description = "Manually create seats for a specific showtime (Admin only)")
    public ResponseObject<List<SeatResponse>> createSeatsForShowtime(@PathVariable Long showtimeId) {
        log.info("Manually creating seats for showtime id: {}", showtimeId);
        return new ResponseObject<>(HttpStatus.OK, "Success", seatService.getAvailableSeatByShowtime(showtimeId));
    }
}
