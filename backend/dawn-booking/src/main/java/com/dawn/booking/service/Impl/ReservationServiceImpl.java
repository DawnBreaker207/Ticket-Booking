package com.dawn.booking.service.Impl;

import com.dawn.booking.dto.request.*;
import com.dawn.booking.dto.response.*;
import com.dawn.booking.helper.ReservationMappingHelper;
import com.dawn.booking.helper.ReservationNotificationHelper;
import com.dawn.booking.model.Reservation;
import com.dawn.booking.repository.ReservationRepository;
import com.dawn.booking.service.*;
import com.dawn.booking.utils.ReservationUtils;
import com.dawn.common.core.constant.Message;
import com.dawn.common.core.constant.ReservationStatus;
import com.dawn.common.core.constant.SeatStatus;
import com.dawn.common.core.dto.response.ResponsePage;
import com.dawn.common.core.exception.wrapper.PermissionDeniedException;
import com.dawn.common.core.exception.wrapper.ResourceNotFoundException;
import com.dawn.common.core.exception.wrapper.SeatUnavailableException;
import com.dawn.common.core.helper.RedisKeyHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private static final Duration HOLD_TIMEOUT = Duration.ofMinutes(15);

    private final ReservationRepository reservationRepository;

    private final SeatClientService seatService;

    private final UserClientService userService;

    private final ShowtimeClientService showtimeService;

    private final MovieClientBookingService movieService;

    private final ReservationNotificationHelper reservationNotificationHelper;

    private final ReservationRedisService reservationRedisService;

    @Override
    public ResponsePage<UserReservationResponse> findByUser(ReservationUserRequest request, Pageable pageable) {
        log.debug("Finding reservation for user {}, status={}", request.getUserId(), request.getStatus());

        Page<Reservation> reservations = reservationRepository.findAllByUserIdAndReservationStatusOrderByCreatedAtDesc(request.getUserId(), ReservationStatus.CONFIRMED, pageable);

        log.info("Found {} reservations for user {}", reservations.getSize(), request.getUserId());

        return ResponsePage.of(reservations
                .map(reservation -> {
                    ShowtimeDTO showtime = showtimeService.findById(reservation.getShowtimeId());

                    MovieDTO movie = movieService.findOne(showtime.getMovieId());

                    List<SeatDTO> seats = seatService.findAllByReservationId(reservation.getId());
                    return ReservationMappingHelper.toUserResponse(reservation, movie, showtime, seats);
                }));
    }

    @Override
    public ResponsePage<ReservationResponse> findAll(ReservationFilterRequest req, Pageable pageable) {
        LocalDate end = req.getEndDate() != null ? req.getEndDate() : LocalDate.now();
        LocalDate start = req.getStartDate() != null ? req.getStartDate() : end.minusDays(30);

        //  Convert to Instant
        Instant startDate = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endDate = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        return ResponsePage.of(reservationRepository
                .findAllWithFilter(req, startDate, endDate, pageable)
                .map(reservation -> {
                    List<SeatDTO> seats = seatService.findAllByReservationId(reservation.getId());
                    ShowtimeDTO showtime = showtimeService.findById(reservation.getShowtimeId());
                    UserDTO user = userService.findById(reservation.getUserId());
                    return ReservationMappingHelper.map(reservation, user, showtime, seats);
                }));
    }

    @Override
    public ReservationResponse findOne(String id) {
        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.RESERVATION_NOT_FOUND));

        List<SeatDTO> seats = seatService.findAllByReservationId(reservation.getId());
        ShowtimeDTO showtime = showtimeService.findById(reservation.getShowtimeId());
        MovieDTO movie = movieService.findOne(showtime.getMovieId());
        UserDTO user = userService.findById(reservation.getUserId());
        return ReservationMappingHelper.map(reservation, user, showtime, seats);
    }

    @Override
    public ReservationInitResponse restoreReservation(String reservationId) {
        log.info("Restore reservation with id: {}", reservationId);
        Long ttl = reservationRedisService.getReservationTtl(reservationId);

        if (ttl == null || ttl <= 0) {
            log.warn("Reservation {} not found or expired in Redis", reservationId);
            throw new ResourceNotFoundException(Message.Exception.RESERVATION_EXPIRED);
        }

        Map<Object, Object> reservation = reservationRedisService.getReservationData(reservationId);

        if (reservation == null || reservation.isEmpty()) {
            throw new ResourceNotFoundException(Message.Exception.RESERVATION_NOT_FOUND);
        }

        String showtimeIdStr = (String) reservation.get("showtimeId");
        Instant expiredAt = Instant.now().plusSeconds(ttl);

        return ReservationInitResponse.builder()
                .reservationId(reservationId)
                .showtimeId(Long.valueOf(showtimeIdStr))
                .ttl(ttl)
                .expiredAt(expiredAt)
                .build();
    }

    @Override
    public ReservationInitResponse initReservation(ReservationInitRequest o) {
        log.info("Initializing reservation for user {} at showtime {}", o.getUserId(), o.getShowtimeId());

        String reservationId = ReservationUtils.generateReservationIds();

        //        Create essential value to save on redis
        Map<String, String> initialData = Map.of(
                "reservationId", reservationId,
                "userId", o.getUserId().toString(),
                "showtimeId", o.getShowtimeId().toString(),
                "theaterId", o.getTheaterId().toString(),
                "seats", "[]");

        //        Create expired time on redis key
        reservationRedisService.saveReservationInit(reservationId, initialData, HOLD_TIMEOUT);

        log.info("Reservation initialize, Id: {}, user Id: {}, showtime Id: {}, theater Id: {} , ttl: {}",
                reservationId,
                o.getUserId(),
                o.getShowtimeId(),
                o.getTheaterId(),
                HOLD_TIMEOUT.toSeconds());

        return ReservationInitResponse.builder()
                .reservationId(reservationId)
                .showtimeId(o.getShowtimeId())
                .ttl(HOLD_TIMEOUT.toSeconds())
                .expiredAt(Instant.now().plusSeconds(HOLD_TIMEOUT.toSeconds()))
                .build();
    }

    public void holdReservationSeats(ReservationHoldSeatRequest reservation) {
        Long userId = reservation.getUserId();
        String reservationId = reservation.getReservationId();
        Long showtimeId = reservation.getShowtimeId();
        List<Long> seatIds = reservation.getSeatIds();
        boolean success = false;
        log.info("Holding {} seats for reservation {} (user:{}, showtime: {})", seatIds.size(), reservationId, userId, showtimeId);

        //        Take reservationId and userId on redis
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);
        //        Define locks to update seat
        List<Long> successfulLockedSeatIds = new ArrayList<>();
        try {
            Map<Object, Object> reservationData = reservationRedisService.getReservationData(reservationId);
            validateReservationOwnership(reservationData, reservationId, userId);

            validateShowtimeAndAvailability(showtimeId, seatIds.size());

            // Delete old seats in Redis
            List<Long> oldSeatIds = reservationRedisService.parseSeatIdsFromReservationData(reservationData);
            reservationRedisService.removeOldSeatLocks(oldSeatIds, seatIds, redisKey);

            //        Load seats from DB and validate
            List<SeatDTO> seats = seatService.findAllById(seatIds);
            validateSeatsForReservation(seats, showtimeId, seatIds);

            successfulLockedSeatIds = reservationRedisService.acquireSeatLock(seatIds, seats, redisKey);
            //        Update seat in redis
            reservationRedisService.updateReservationSeats(reservationId, seatIds);

            reservationNotificationHelper.getSeatHold(showtimeId, userId);
            success = true;
            log.info("Successfully hold {} seats with user id {} for reservation {}: {} ", seatIds.size(), userId, reservationId, seatIds);
        } finally {
            if (!success && !successfulLockedSeatIds.isEmpty()) {
                log.warn("Rolling back locks for reservation {}", reservationId);
                reservationRedisService.rollbackLocks(successfulLockedSeatIds, redisKey);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ReservationResponse confirmReservation(ReservationRequest request) {
        log.info("Confirming reservation: {}", request.getReservationId());
        //        Get reservation id from redis
        String reservationId = request.getReservationId();
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);

        ReservationRedisDTO cachedData = reservationRedisService.getFromRedis(reservationId);

        if (request.getUserId() == null || !cachedData.getUserId().equals(request.getUserId())) {
            throw new PermissionDeniedException("User mismatch or invalid user");
        }

        log.info("Reservation hold get info: {}", cachedData);
        List<Long> seatIds = cachedData.getSeatsIds();
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalStateException(Message.Exception.NO_SEAT_SELECTED);
        }

        reservationRedisService.validateSeatLocks(redisKey, reservationId, seatIds);
        //        Create reservation to save
        List<SeatDTO> seatEntities = loadSeatFromDatabase(seatIds, reservationId);
        validateSeatsStillAvailable(seatEntities);

        log.info("All {} seats verified as available in DB for reservation {}", seatEntities.size(), reservationId);
        ShowtimeDTO showtime = showtimeService.findById(request.getShowtimeId());
        BigDecimal total = showtime.getPrice().multiply(BigDecimal.valueOf(seatEntities.size()));
        UserDTO user = userService.findById(request.getUserId());
        log.info("Calculated total amount: {} for {} seats", total, seatEntities.size());

        Reservation reservation = Reservation
                .builder()
                .id(request.getReservationId())
                .userId(cachedData.getUserId())
                .showtimeId(showtime.getId())
                .reservationStatus(ReservationStatus.CONFIRMED)
                .totalAmount(total)
                .isPaid(true)
                .isDeleted(false)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        updateSeatsAndShowtime(seatEntities, savedReservation);

        reservationNotificationHelper.handlePaymentAndNotification(savedReservation, showtime, seatEntities);

        reservationRedisService.cleanupRedisLocks(redisKey, reservationId, seatEntities);

        log.info("Successfully confirmed reservation: {} with {} seats", reservation.getId(), seatEntities.size());
        return ReservationMappingHelper.map(savedReservation, user, showtime, seatEntities);
    }

    @Override
    @Transactional
    public void cancelReservation(String reservationId, Long userId) {
        log.info("Cancel reservation {} for user {}", reservationId, userId);

        //        Get reservation id from redis
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);
        ReservationRedisDTO cachedData = reservationRedisService.getFromRedis(reservationId);
        if (!cachedData.getUserId().equals(userId)) {
            throw new PermissionDeniedException("You don't have permission to confirm this reservation");
        }

        //      Load seats from DB
        List<Long> seatIds = cachedData.getSeatsIds();
        if (seatIds != null && !seatIds.isEmpty()) {
            reservationRedisService.deleteSeatLocks(seatIds, redisKey);
        }
        //        Take seat from request
        List<SeatDTO> seatEntities = seatService.findByIdWithLock(seatIds);
        //        Create reservation to save
        ShowtimeDTO showtime = showtimeService.findById(cachedData.getShowtimeId());
        UserDTO user = userService.findById(userId);

        BigDecimal total = showtime.getPrice().multiply(BigDecimal.valueOf(seatEntities.size()));
        log.info("Calculated total amount: {} for {} seats", total, seatEntities.size());


        Reservation reservation = Reservation
                .builder()
                .id(reservationId)
                .userId(user.getId())
                .showtimeId(showtime.getId())
                .reservationStatus(ReservationStatus.CANCELED)
                .totalAmount(total)
                .isPaid(false)
                .isDeleted(false)
                .build();

        reservationRepository.save(reservation);
        log.info("Updated reservation {} status to FAILED", reservationId);

        reservationRedisService.deleteReservation(reservationId);
        log.info("Remove reservation hold key {}", redisKey);

        reservationNotificationHelper.getSeatRelease(showtime.getId(), userId);
        log.info("Published seat release event for showtime {}: {}", showtime.getId(), seatIds);

        log.info("Cancelled reservation hold {} successfully", reservationId);
    }

    private void validateSeatsForReservation(List<SeatDTO> seats, Long showtimeId, List<Long> seatIds) {
        if (seats.size() != seatIds.size()) {
            List<Long> foundSeatIds = seats
                    .stream()
                    .map(SeatDTO::getId)
                    .toList();
            List<Long> notFoundSeatIds = seatIds
                    .stream()
                    .filter(id -> !foundSeatIds.contains(id))
                    .toList();
            throw new SeatUnavailableException(Message.Exception.SEAT_NOT_FOUND + notFoundSeatIds);
        }
        //        Verify seats belong to the request showtime
        List<String> wrongShowtimeSeats = new ArrayList<>();
        for (SeatDTO seat : seats) {
            if (!seat.getShowtimeId().equals(showtimeId)) {
                wrongShowtimeSeats.add(seat.getSeatNumber());
            }
        }

        if (!wrongShowtimeSeats.isEmpty()) {
            String wrongSeatNumbers = String.join(", ", wrongShowtimeSeats);
            throw new SeatUnavailableException("Seats " + wrongSeatNumbers + " do not belong to the requested showtime");
        }

        //        Check seat in showtime was booked in database
        List<String> bookedSeats = new ArrayList<>();
        for (SeatDTO seat : seats) {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                bookedSeats.add(seat.getSeatNumber());
            }
        }
        if (!bookedSeats.isEmpty()) {
            String bookedSeatNumbers = String.join(", ", bookedSeats);
            throw new SeatUnavailableException(Message.Exception.SEAT_UNAVAILABLE + " " + bookedSeatNumbers);
        }

    }

    private void validateSeatsStillAvailable(List<SeatDTO> seats) {
        List<String> unavailableSeats = new ArrayList<>();
        for (SeatDTO seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                unavailableSeats.add(seat.getSeatNumber());
            }
        }
        if (!unavailableSeats.isEmpty()) {
            throw new SeatUnavailableException(Message.Exception.SEAT_UNAVAILABLE + " " + String.join(", ", unavailableSeats));
        }

    }

    private void validateReservationOwnership(Map<Object, Object> reservationData, String reservationId, Long userId) {
        if (reservationData == null || reservationData.isEmpty()) {
            throw new ResourceNotFoundException(Message.Exception.RESERVATION_NOT_FOUND);
        }
        String userIdStr = (String) reservationData.get("userId");
        if (!userIdStr.equals(String.valueOf(userId))) {
            throw new PermissionDeniedException(Message.Exception.PERMISSION_FORBIDDEN);
        }
        //        Check and compare value valid
        String reservationIdStr = (String) reservationData.get("reservationId");
        if (reservationIdStr == null || !reservationIdStr.equals(reservationId)) {
            throw new ResourceNotFoundException("Invalid reservation data");
        }
        //        Check user existed
        userService.findById(userId);
    }

    private ShowtimeDTO validateShowtimeAndAvailability(Long showtimeId, int requestSeats) {
        //      Validate showtime
        ShowtimeDTO showtime = showtimeService.findById(showtimeId);
        //      Check showtime is in the past
        if (showtime.getShowDate().isBefore((LocalDate.now())) ||
                (showtime.getShowDate().isEqual(LocalDate.now()) &&
                        showtime.getShowTime().isBefore(LocalTime.now()))) {
            throw new IllegalStateException("Cannot reserve seats for past showtime");
        }

        //        Check if showtime has enough available seats
        if (showtime.getAvailableSeats() < requestSeats) {
            throw new IllegalStateException(String.format("Not enough available seats. Request: %d, Available: %d", requestSeats, showtime.getAvailableSeats()));
        }
        return showtime;
    }

    //    Reservation private method
    private List<SeatDTO> loadSeatFromDatabase(List<Long> seatIds, String reservationId) {
        //        Take seat from request
        List<SeatDTO> seats = seatService.findByIdWithLock(seatIds);
        if (seats.size() != seatIds.size()) {
            log.error("Expected {} seats but found {} for reservation {}", seatIds.size(), seats.size(), reservationId);
            throw new IllegalStateException("Some seats not found in database");
        }

        return seats;
    }

    private void updateSeatsAndShowtime(List<SeatDTO> seats, Reservation reservation) {
        ShowtimeDTO showtime = showtimeService.findById(reservation.getShowtimeId());
        int newAvailable = showtime.getAvailableSeats() - seats.size();
        if (newAvailable < 0) {
            log.error("Showtime {} available seats would become negative: {}, Current: {}", showtime.getId(), newAvailable, showtime.getAvailableSeats());
            throw new IllegalStateException("Showtime available seats calculation error");
        }

        seats.forEach(seat -> {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setReservationId(reservation.getId());
        });

        //      Update Unavailable seats count in showtime
        seatService.saveAllSeat(seats);
        log.info("Updated {} seats to BOOKED status", seats.size());
        showtime.setAvailableSeats(newAvailable);
        showtimeService.save(showtime);
    }


}
