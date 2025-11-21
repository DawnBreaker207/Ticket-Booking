package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.dto.request.*;
import com.dawn.backend.dto.response.ReservationInitResponseDTO;
import com.dawn.backend.dto.response.ReservationResponseDTO;
import com.dawn.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@Tag(name = "Reservation", description = "Operations related to reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Get all reservation with conditions", description = "Returns reservation with condition filters (Admin Only)")
    public ResponseObject<List<ReservationResponseDTO>> getAll(@ModelAttribute ReservationFilterDTO o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.findAll(o));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by id", description = "Returns reservation by its Id (Admin Only)")
    public ResponseObject<ReservationResponseDTO> getOne(@PathVariable String id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.findOne(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get reservation by user id", description = "Returns reservation by they own Id (User Only)")
    public ResponseObject<List<ReservationResponseDTO>> getAllByUser(@RequestParam ReservationUserRequestDTO request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.findByUser(request.getIsPaid(), request.getStatus()));
    }

    @PostMapping("/init")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Init a reservation", description = "Create a reservation and return Id")
    public ResponseObject<ReservationInitResponseDTO> reservationInit(@RequestBody ReservationInitRequestDTO reservation) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.initReservation(reservation));
    }

    @PostMapping("/seatHold")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Choose and booking seat", description = "Selected seat place and booking it")
    public ResponseObject<Void> reservationHoldSeat(@RequestBody ReservationHoldSeatRequestDTO o) {
        reservationService.holdReservationSeats(o);
        return new ResponseObject<>(HttpStatus.OK, "Success", null);
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Save reservation after payment ", description = "Returns reservation after booking seats and payment success")
    public ResponseObject<ReservationResponseDTO> reservationConfirm(@RequestBody ReservationRequestDTO o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", reservationService.confirmReservation(o));
    }

    @PostMapping("/{reservationId}/cancel")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Cancel reservation after payment ", description = "Cancel reservation after booking seats and payment failed")
    public ResponseObject<Void> reservationCancel(@PathVariable String reservationId, @RequestBody Long userId) {
        reservationService.cancelReservation(reservationId, userId);
        return new ResponseObject<>(HttpStatus.OK, "Success", null);
    }

}
