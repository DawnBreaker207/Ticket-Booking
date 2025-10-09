package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.ReservationFilterDTO;
import com.example.backend.dto.request.ReservationHoldSeatRequestDTO;
import com.example.backend.dto.request.ReservationInitRequestDTO;
import com.example.backend.dto.request.ReservationRequestDTO;
import com.example.backend.dto.response.ReservationResponseDTO;
import com.example.backend.model.Reservation;
import com.example.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@Tag(name = "Reservation", description = "Operations related to reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<List<ReservationResponseDTO>> getAll(@ModelAttribute ReservationFilterDTO o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.findAll(o));
    }

    @GetMapping("/{id}")
    public ResponseObject<Reservation> getOne(@PathVariable String id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.findOne(id));
    }

    @PostMapping("/init")
    public ResponseObject<String> reservationInit(@RequestBody ReservationInitRequestDTO reservation) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.initReservation(reservation));
    }

    @PostMapping("/seatHold")
    public ResponseObject<Void> reservationHoldSeat(@RequestBody ReservationHoldSeatRequestDTO o) {
        reservationService.holdSeats(o);
        return new ResponseObject<>(HttpStatus.OK, "Success", null);
    }

    @PostMapping("/confirm")
    public ResponseObject<ReservationResponseDTO> reservationConfirm(@RequestBody ReservationRequestDTO o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.confirm(o));
    }

}
