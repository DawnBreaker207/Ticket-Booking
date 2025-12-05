package com.dawn.backend.service.Impl;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.constant.Message;
import com.dawn.backend.constant.PaymentStatus;
import com.dawn.backend.constant.ReservationStatus;
import com.dawn.backend.constant.SeatStatus;
import com.dawn.backend.dto.request.*;
import com.dawn.backend.dto.response.ReservationInitResponse;
import com.dawn.backend.dto.response.ReservationResponse;
import com.dawn.backend.dto.response.UserReservationResponse;
import com.dawn.backend.exception.wrapper.*;
import com.dawn.backend.helper.RedisKeyHelper;
import com.dawn.backend.helper.ReservationMappingHelper;
import com.dawn.backend.model.*;
import com.dawn.backend.repository.*;
import com.dawn.backend.service.NotificationService;
import com.dawn.backend.service.RedisService;
import com.dawn.backend.service.ReservationService;
import com.dawn.backend.util.ReservationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private static final Duration HOLD_TIMEOUT = Duration.ofMinutes(15);

    private final ReservationRepository reservationRepository;

    private final SeatRepository seatRepository;

    private final UserRepository userRepository;

    private final ShowtimeRepository showtimeRepository;

    private final NotificationService notificationService;

    private final PaymentRepository paymentRepository;

    private final ObjectMapper mapper;

    private final RedisService redisService;

    @Override
    public ResponsePage<UserReservationResponse> findByUser(ReservationUserRequest request, Pageable pageable) {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl user)) {
//            log.warn("No authenticated user found");
//            return new ResponsePage<>(Page.empty());
//        }
//        Long userId = user.getId();

        log.debug("Finding reservation for user {} with isPaid={}, status={}", request.getUserId(), request.getStatus());

        Page<Reservation> reservations = reservationRepository.findAllByUserIdAndReservationStatus(request.getUserId(), ReservationStatus.CONFIRMED, pageable);

        log.info("Found {} reservations for user {}", reservations.getSize(), request.getUserId());

        return ResponsePage.of(reservations
                .map(ReservationMappingHelper::toUserResponse));
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
                .map(ReservationMappingHelper::map));
    }

    @Override
    public ReservationResponse findOne(String id) {
        return reservationRepository
                .findById(id)
                .map(ReservationMappingHelper::map)
                .orElseThrow(() -> new ReservationNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.RESERVATION_NOT_FOUND));
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
        redisService.saveReservationInit(reservationId, initialData, HOLD_TIMEOUT);

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
            Map<Object, Object> reservationData = redisService.getReservationData(reservationId);
            validateReservationOwnership(reservationData, reservationId, userId);

            validateShowtimeAndAvailability(showtimeId, seatIds.size());

            // Delete old seats in Redis
            List<Long> oldSeatIds = parseSeatIdsFromReservationData(reservationData);
            removeOldSeatLocks(oldSeatIds, seatIds, redisKey);

            //        Load seats from DB and validate
            List<Seat> seats = seatRepository.findAllById(seatIds);
            validateSeatsForReservation(seats, showtimeId, seatIds);

            successfulLockedSeatIds = acquireSeatLock(seatIds, seats, redisKey);
            //        Update seat in redis
            redisService.updateReservationSeats(reservationId, seatIds);

            List<Map<String, Object>> seatInfo = notificationService.getSeatSnapshot(showtimeId);
            Map<String, Object> event = Map.of(
                    "event", "SEAT_HOLD",
                    "showtimeId", showtimeId,
                    "userId", userId,
                    "seatIds", seatInfo
            );
            //            Send notification via pub sub
            redisService.publishSeatEvent(showtimeId, event);
            success = true;
            log.info("Successfully hold {} seats with user id {} for reservation {}: {} ", seatIds.size(), userId, reservationId, seatIds);
        } finally {
            if (!success && !successfulLockedSeatIds.isEmpty()) {
                log.warn("Rolling back locks for reservation {}", reservationId);
                rollbackLocks(successfulLockedSeatIds, redisKey);
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ReservationResponse confirmReservation(ReservationRequest request) {
        log.info("Confirming reservation: {}", request.getReservationId());

        //        Get reservation id from redis
        String reservationId = request.getReservationId();
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);

        Reservation dto = validateUserAndGetReservation(request, reservationId);

        List<Long> seatIds = extractSeatIds(dto);

        validateSeatLocks(redisKey, reservationId, seatIds);
        //        Create reservation to save
        Showtime showtime = showtimeRepository
                .findById(request.getShowtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with id : " + request.getShowtimeId()));

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User now found with id :" + request.getUserId()));

        List<Seat> seatEntities = loadSeatFromDatabase(seatIds, reservationId);

        validateSeatsStillAvailable(seatEntities);
        log.info("All {} seats verified as available in DB for reservation {}", seatEntities.size(), reservationId);


        BigDecimal total = showtime.getPrice().multiply(BigDecimal.valueOf(seatEntities.size()));
        log.info("Calculated total amount: {} for {} seats", total, seatEntities.size());

        Reservation reservation = Reservation
                .builder()
                .id(request.getReservationId())
                .user(user)
                .showtime(showtime)
                .reservationStatus(ReservationStatus.CONFIRMED)
                .seats(seatEntities)
                .totalAmount(total)
                .isPaid(true)
                .isDeleted(false)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        updateSeatsAndShowtime(seatEntities, savedReservation);

        if (ReservationStatus.CONFIRMED.equals(reservation.getReservationStatus())) {
            var payment = updatePayment(reservation, true);
            notificationService.sendEmail(
                    user.getEmail(),
                    user.getUsername(),
                    reservationId,
                    showtime.getMovie().getTitle(),
                    showtime.getTheater().getName(),
                    LocalDateTime
                            .of(showtime.getShowDate(), showtime.getShowTime())
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    seatEntities.stream().map(Seat::getSeatNumber).collect(Collectors.joining(",")),
                    LocalDateTime.ofInstant(payment.getCreatedAt(), ZoneId.of("Asia/Ho_Chi_Minh"))
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                    ,
                    reservation.getTotalAmount().toString()
            );
        } else {
            updatePayment(reservation, false);
        }

        cleanupRedisLocks(redisKey, reservationId, seatEntities);

        log.info("Successfully confirmed reservation: {} with {} seats", reservation.getId(), seatEntities.size());
        return ReservationMappingHelper.map(savedReservation);
    }

    @Override
    @Transactional
    public void cancelReservation(String reservationId, Long userId) {
        log.info("Cancel reservation {} for user {}", reservationId, userId);

        //        Get reservation id from redis
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);
        Reservation reservationHold = getFromRedis(reservationId);
        if (!reservationHold.getUser().getId().equals(userId)) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permission to confirm this reservation");
        }

        //      Load seats from DB
        List<Long> seatIds = extractSeatIds(reservationHold);

        deleteSeatLocks(seatIds, redisKey);

        //        Take seat from request
        List<Seat> seatEntities = seatRepository.findByIdWithLock(seatIds);
        //        Create reservation to save
        Showtime showtime = showtimeRepository
                .findById(reservationHold.getShowtime().getId())
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with id : " + reservationHold.getShowtime().getId()));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User now found with id :" + userId));

        BigDecimal total = showtime.getPrice().multiply(BigDecimal.valueOf(seatEntities.size()));
        log.info("Calculated total amount: {} for {} seats", total, seatEntities.size());


        Reservation reservation = Reservation
                .builder()
                .id(reservationId)
                .user(user)
                .showtime(showtime)
                .reservationStatus(ReservationStatus.CANCELED)
                .seats(seatEntities)
                .totalAmount(total)
                .isPaid(false)
                .isDeleted(false)
                .build();

        reservationRepository.save(reservation);
        log.info("Updated reservation {} status to FAILED", reservationId);

        redisService.deleteReservation(reservationId);
        log.info("Remove reservation hold key {}", redisKey);

        List<Map<String, Object>> seatInfo = notificationService.getSeatSnapshot(showtime.getId());
        Map<String, Object> event = Map.of(
                "event", "SEAT_RELEASE",
                "showtimeId", showtime.getId(),
                "seatIds", seatInfo,
                "userId", userId
        );

        //            Send notification via pub sub
        redisService.publishSeatEvent(showtime.getId(), event);
        log.info("Published seat release event for showtime {}: {}", showtime.getId(), seatIds);

        log.info("Cancelled reservation hold {} successfully", reservationId);
    }

    private void deleteSeatLocks(List<Long> seatIds, String redisKey) {
        for (Long seatId : seatIds) {
            Boolean deleted = redisService.deleteSeatLockIfOwner(seatId, redisKey);
            if (deleted) {
                log.warn("Release lock for seat {} in", seatId);
            } else {
                log.warn("Seat {} lock not owned by this reservation, skip", seatId);
            }
        }
        log.info("ALl Redis locks verified for reservation {}", redisKey);
    }

    private List<Long> acquireSeatLock(List<Long> seatIds, List<Seat> seats, String redisKey) {
        List<Long> successfulSeatIds = new ArrayList<>();
        List<SeatLockFailure> failures = new ArrayList<>();
        //        Check seat in showtime was booked in redis
        for (Long seatId : seatIds) {
            //            Check seat lock in redis
            Boolean acquired = redisService.lockSeat(seatId, redisKey, HOLD_TIMEOUT);
            //            Get owner of this seat
            String owner = redisService.getSeatOwner(seatId);
            if (acquired) {
                successfulSeatIds.add(seatId);
                log.debug("Successfully locked seat {} for reservation {}", seatId, redisKey);
                continue;
                //                Check the right owner
            }
            if (redisKey.equals(owner)) {
                redisService.refreshSeatLockIfOwner(seatId, redisKey, HOLD_TIMEOUT);
                log.debug("Refreshed existing lock for seat {} in reservation {}", seatId, redisKey);
                successfulSeatIds.add(seatId);
            } else {
                //                Check if the seat was hold by another owner
                Seat seat = seats.stream().filter(s -> s
                                .getId()
                                .equals(seatId))
                        .findFirst()
                        .orElse(null);
                String seatNumber = seat != null ? seat.getSeatNumber() : seatId.toString();
                failures.add(new SeatLockFailure(seatId, seatNumber, owner));
                log.warn("Seat {} ({}) is hold by reservation {}", seatId, seatNumber, owner);
            }
        }

        //        If one of the list fail, return this exception
        if (!failures.isEmpty()) {
            log.warn("Failed to lock {} for seats for reservation {}. Rolling back {} successful locks", failures.size(), redisKey, successfulSeatIds.size());
            rollbackLocks(successfulSeatIds, redisKey);
            String failedSeatMessage = failures
                    .stream()
                    .map(f -> f.seatNumber + " (hold by " + f.ownerReservationId + ")")
                    .collect(Collectors.joining((", ")));
            throw new SeatUnavailableException(HttpStatus.CONFLICT, "Cannot hold seats. The following seats are currently hold by other users: " + failedSeatMessage);
        }
        return successfulSeatIds;
    }

    private void validateSeatsForReservation(List<Seat> seats, Long showtimeId, List<Long> seatIds) {
        if (seats.size() != seatIds.size()) {
            List<Long> foundSeatIds = seats
                    .stream()
                    .map(Seat::getId)
                    .toList();
            List<Long> notFoundSeatIds = seatIds
                    .stream()
                    .filter(id -> !foundSeatIds.contains(id))
                    .toList();
            throw new SeatUnavailableException(Message.Exception.SEAT_NOT_FOUND + notFoundSeatIds);
        }
        //        Verify seats belong to the request showtime
        List<String> wrongShowtimeSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (!seat.getShowtime().getId().equals(showtimeId)) {
                wrongShowtimeSeats.add(seat.getSeatNumber());
            }
        }

        if (!wrongShowtimeSeats.isEmpty()) {
            String wrongSeatNumbers = String.join(", ", wrongShowtimeSeats);
            throw new SeatUnavailableException("Seats " + wrongSeatNumbers + " do not belong to the requested showtime");
        }

        //        Check seat in showtime was booked in database
        List<String> bookedSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                bookedSeats.add(seat.getSeatNumber());
            }
        }
        if (!bookedSeats.isEmpty()) {
            String bookedSeatNumbers = String.join(", ", bookedSeats);
            throw new SeatUnavailableException(HttpStatus.CONFLICT, Message.Exception.SEAT_UNAVAILABLE + " " + bookedSeatNumbers);
        }

    }

    private void validateSeatsStillAvailable(List<Seat> seats) {
        List<String> unavailableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                unavailableSeats.add(seat.getSeatNumber());
            }
        }
        if (!unavailableSeats.isEmpty()) {
            throw new SeatUnavailableException(HttpStatus.CONFLICT, Message.Exception.SEAT_UNAVAILABLE + " " + String.join(", ", unavailableSeats));
        }

    }

    //    Get data from redis
    private Reservation getFromRedis(String reservationId) {
        Map<Object, Object> data = redisService.getReservationData(reservationId);
        if (data == null || data.isEmpty()) {
            throw new ReservationExpiredException(HttpStatus.NOT_FOUND, Message.Exception.RESERVATION_EXPIRED);
        }
        Reservation dto = new Reservation();
        dto.setId(reservationId);

        Long userId = safeParseLong((String) data.get("userId"), "userId");
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Message.Exception.USER_NOT_FOUND));
        dto.setUser(user);

        Long showtimeId = safeParseLong((String) data.get("showtimeId"), "showtimeId");
        Showtime showtime = showtimeRepository
                .findById(showtimeId)
                .orElseThrow(() -> new ShowtimeNotFoundException(Message.Exception.SHOWTIME_NOT_FOUND));
        dto.setShowtime(showtime);
        try {
            String seatJson = (String) data.get("seats");
            List<Long> seatIds = mapper.readValue(seatJson, new TypeReference<>() {
            });
            if (!seatIds.isEmpty()) {
                List<Seat> seats = seatRepository.findAllById(seatIds);
                dto.setSeats(seats);
            } else {
                dto.setSeats(new ArrayList<>());
            }
        } catch (JsonProcessingException ex) {
            log.error("Error parsing seat IDs from Redis", ex);
            throw new RedisStorageException(HttpStatus.NOT_FOUND, "Info in redis not exists or error when getting that");
        }
        return dto;
    }

    //        Clean up Redis
    private void cleanupRedisLocks(String redisKey, String reservationId, List<Seat> seats) {
        int deletedLocks = 0;
        for (Seat seat : seats) {
            Boolean deleted = redisService.deleteSeatLockIfOwner(seat.getId(), redisKey);
            if (deleted) {
                deletedLocks++;
                log.debug("Deleted lock for seat {}", seat.getId());
            } else {
                String currentOwner = redisService.getSeatOwner(seat.getId());
                log.warn("Lock for seat {} has unexpected owner: {}. Expected: {}", seat.getId(), currentOwner, redisKey);
            }
        }
        redisService.deleteReservation(reservationId);
        log.info("Cleaned up Redis: {} seat locks deleted, reservation key deleted", deletedLocks);
    }

    private Long safeParseLong(String value, String fieldName) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("Invalid {} format in Redis: {}", fieldName, value);
            throw new RedisStorageException(HttpStatus.BAD_REQUEST, "Invalid " + fieldName + " format in Redis");
        }
    }

    private void validateReservationOwnership(Map<Object, Object> reservationData, String reservationId, Long userId) {
        if (reservationData == null || reservationData.isEmpty()) {
            throw new ReservationNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, Message.Exception.RESERVATION_NOT_FOUND);
        }
        String userIdStr = (String) reservationData.get("userId");
        if (!userIdStr.equals(String.valueOf(userId))) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, Message.Exception.PERMISSION_FORBIDDEN);
        }
        //        Check and compare value valid
        String reservationIdStr = (String) reservationData.get("reservationId");
        if (reservationIdStr == null || !reservationIdStr.equals(reservationId)) {
            throw new ReservationNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid reservation data");
        }
        //        Check user existed
        userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Message
                        .Exception
                        .USER_NOT_FOUND));
    }

    private Showtime validateShowtimeAndAvailability(Long showtimeId, int requestSeats) {
        //      Validate showtime
        Showtime showtime = showtimeRepository
                .findById(showtimeId)
                .orElseThrow(() -> new TheaterNotFoundException(Message
                        .Exception
                        .SHOWTIME_NOT_FOUND));
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

    private List<Long> parseSeatIdsFromReservationData(Map<Object, Object> reservationData) {
        try {
            String currentSeatsJson = (String) reservationData.get("seats");
            if (currentSeatsJson == null || currentSeatsJson.isEmpty()) {
                return Collections.emptyList();
            }
            return mapper.readValue(currentSeatsJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RedisStorageException(HttpStatus.NOT_FOUND, "Info in redis not exists or error when getting that");
        }
    }

    private void removeOldSeatLocks(Collection<Long> oldSeatIds, Collection<Long> newSeatIds, String redisKey) {
        for (Long oldSeatId : oldSeatIds) {
            if (!newSeatIds.contains(oldSeatId)) {
                redisService.deleteSeatLockIfOwner(oldSeatId, redisKey);
            }
        }
    }

    private void rollbackLocks(Collection<Long> seatIds, String expectedOwner) {
        if (seatIds == null || seatIds.isEmpty()) return;
        log.info("Rolling back {} seat lock for reservation {}", seatIds.size(), expectedOwner);
        for (Long seatId : seatIds) {
            try {
                String currentOwner = redisService.getSeatOwner(seatId);
                if (expectedOwner.equals(currentOwner)) {
                    redisService.deleteSeatLockIfOwner(seatId, expectedOwner);
                } else {
                    log.warn("Lock {} ownership changed during rollback. Expected: {}, Current: {}", seatId, expectedOwner, currentOwner);
                }
            } catch (Exception ex) {
                log.error("Failed to rollback lock {}", seatId, ex);
            }
        }
    }

    //    Reservation private method

    private Reservation validateUserAndGetReservation(ReservationRequest request, String reservationId) {
        Reservation dto = getFromRedis(reservationId);
        if (!dto.getUser().getId().equals(request.getUserId())) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permission to confirm this reservation");
        }
        return dto;
    }

    private List<Long> extractSeatIds(Reservation dto) {
        //      Load seats from DB
        List<Long> seatIds = dto
                .getSeats()
                .stream()
                .map(Seat::getId)
                .toList();
        if (seatIds.isEmpty()) {
            log.warn("No seats to release for reservation {}", dto.getId());
            throw new IllegalStateException(Message.Exception.NO_SEAT_SELECTED);
        }
        return seatIds;
    }

    private void validateSeatLocks(String redisKey, String reservationId, List<Long> seatIds) {
        List<Long> expiredLocks = new ArrayList<>();
        List<Long> stolenLocks = new ArrayList<>();

        for (Long seatId : seatIds) {
            String lockOwner = redisService.getSeatOwner(seatId);
            if (lockOwner == null) {
                expiredLocks.add(seatId);
                log.warn("Lock expired for seat {} in reservation {}", seatId, reservationId);
            } else if (!lockOwner.equals(redisKey)) {
                stolenLocks.add(seatId);
                log.warn("Lock stolen for seat {} in reservation {}, Current owner: {}", seatId, reservationId, lockOwner);
            }
        }

        if (!expiredLocks.isEmpty() || !stolenLocks.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Cannot confirm reservation. ");

            if (!expiredLocks.isEmpty()) {
                errorMsg.append("Your hold on seats ").append(expiredLocks).append(" has expired. ");
            }
            if (!stolenLocks.isEmpty()) {
                errorMsg.append("Seats ").append(stolenLocks).append(" have been taken by other users. ");
            }

            errorMsg.append("Please select seat again");

            log.error("Reservation {} confirmation failed. Expired: {}, Stolen: {}", reservationId, expiredLocks, stolenLocks);
            throw new SeatUnavailableException(HttpStatus.CONFLICT, errorMsg.toString());
        }

        log.info("ALl Redis locks verified for reservation {}", reservationId);
    }

    private List<Seat> loadSeatFromDatabase(List<Long> seatIds, String reservationId) {
        //        Take seat from request
        List<Seat> seats = seatRepository.findByIdWithLock(seatIds);
        if (seats.size() != seatIds.size()) {
            log.error("Expected {} seats but found {} for reservation {}", seatIds.size(), seats.size(), reservationId);
            throw new IllegalStateException("Some seats not found in database");
        }

        return seats;
    }

    private void updateSeatsAndShowtime(List<Seat> seats, Reservation reservation) {
        Showtime showtime = reservation.getShowtime();
        int newAvailable = showtime.getAvailableSeats() - seats.size();
        if (newAvailable < 0) {
            log.error("Showtime {} available seats would become negative: {}, Current: {}", showtime.getId(), newAvailable, showtime.getAvailableSeats());
            throw new IllegalStateException("Showtime available seats calculation error");
        }

        seats.forEach(seat -> {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setReservation(reservation);
        });

        //      Update Unavailable seats count in showtime
        seatRepository.saveAll(seats);
        log.info("Updated {} seats to BOOKED status", seats.size());
        showtime.setAvailableSeats(newAvailable);
        showtimeRepository.save(showtime);
    }

    private Payment updatePayment(Reservation reservation, Boolean success) {
        PaymentStatus status = success ? PaymentStatus.PAID : PaymentStatus.CANCELED;
        paymentRepository.findByReservation(reservation)
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .ifPresent((payment) -> {
                    throw new IllegalStateException(Message.Exception.PAYMENT_COMPLETE);
                });

        Payment payment = Payment
                .builder()
                .reservation(reservation)
                .paymentIntentId(reservation.getId())
                .amount(reservation.getTotalAmount())
                .status(status)
                .createdAt(Instant.now())
                .build();
        return paymentRepository.save(payment);
    }

    @Getter
    @AllArgsConstructor
    private static class SeatLockFailure {
        final Long seatId;
        final String seatNumber;
        final String ownerReservationId;
    }
}
