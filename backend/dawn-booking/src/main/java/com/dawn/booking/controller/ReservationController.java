package com.dawn.booking.controller;

import com.dawn.booking.dto.request.*;
import com.dawn.booking.dto.response.ReservationInitResponse;
import com.dawn.booking.dto.response.ReservationResponse;
import com.dawn.booking.dto.response.UserReservationResponse;
import com.dawn.booking.service.ReservationService;
import com.dawn.common.core.dto.response.ResponseObject;
import com.dawn.common.core.dto.response.ResponsePage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@Tag(name = "Reservation", description = "Operations related to reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Get all reservation with conditions", description = "Returns reservation with condition filters (Admin Only)")
    public ResponseObject<ResponsePage<ReservationResponse>> getAll(@ModelAttribute ReservationFilterRequest o, Pageable pageable) {
        return ResponseObject.success(reservationService.findAll(o, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by id", description = "Returns reservation by its Id (Admin Only)")
    public ResponseObject<ReservationResponse> getOne(@PathVariable String id) {
        return ResponseObject.success(reservationService.findOne(id));
    }

    @GetMapping("/me")
//    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get reservation by user id", description = "Returns reservation by they own Id (User Only)")
    public ResponseObject<ResponsePage<UserReservationResponse>> getAllByUser(@ModelAttribute ReservationUserRequest request, Pageable pageable) {
        return ResponseObject.success(reservationService.findByUser(request, pageable));
    }

    @GetMapping("/{reservationId}/restore")
    @Operation(summary = "Restore a reservation", description = "Restore a reservation and return data")
    public ResponseObject<ReservationInitResponse> restoreReservation(@PathVariable String reservationId){
            return ResponseObject.success(reservationService.restoreReservation(reservationId));
    }

    @PostMapping("/init")
//    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Init a reservation", description = "Create a reservation and return Id")
    public ResponseObject<ReservationInitResponse> reservationInit(@RequestBody ReservationInitRequest reservation) {
        return ResponseObject.success(reservationService.initReservation(reservation));
    }

    @PostMapping("/seatHold")
//    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Choose and booking seat", description = "Selected seat place and booking it")
    public ResponseObject<Void> reservationHoldSeat(@RequestBody ReservationHoldSeatRequest o) {
        reservationService.holdReservationSeats(o);
        return ResponseObject.success(null);
    }

    @PostMapping("/confirm")
//    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Save reservation after payment ", description = "Returns reservation after booking seats and payment success")
    public ResponseObject<ReservationResponse> reservationConfirm(@RequestBody ReservationRequest o) {
        return ResponseObject.created(reservationService.confirmReservation(o));
    }

    @PostMapping("/{reservationId}/cancel")
//    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Cancel reservation after payment ", description = "Cancel reservation after booking seats and payment failed")
    public ResponseObject<Void> reservationCancel(@PathVariable String reservationId, @RequestBody Long userId) {
        reservationService.cancelReservation(reservationId, userId);
        return ResponseObject.success(null);
    }

}
