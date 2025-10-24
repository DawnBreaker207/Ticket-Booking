package com.example.backend.service.Impl;

import com.example.backend.config.redis.RedisPublisher;
import com.example.backend.constant.Message;
import com.example.backend.constant.PaymentStatus;
import com.example.backend.constant.ReservationStatus;
import com.example.backend.constant.SeatStatus;
import com.example.backend.dto.request.ReservationFilterDTO;
import com.example.backend.dto.request.ReservationHoldSeatRequestDTO;
import com.example.backend.dto.request.ReservationInitRequestDTO;
import com.example.backend.dto.request.ReservationRequestDTO;
import com.example.backend.dto.response.ReservationInitResponseDTO;
import com.example.backend.dto.response.ReservationResponseDTO;
import com.example.backend.exception.wrapper.*;
import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.helper.ReservationMappingHelper;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.service.NotificationService;
import com.example.backend.service.RedisService;
import com.example.backend.service.ReservationService;
import com.example.backend.util.ReservationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String, Object> redisTemplate;

    private final ShowtimeRepository showtimeRepository;

    private final NotificationService notificationService;

    private final PaymentRepository paymentRepository;

    private final ObjectMapper mapper;

    private final RedisService redisService;

    private final RedisPublisher redisPublisher;

    @Override
    public List<ReservationResponseDTO> findAll(ReservationFilterDTO o) {
        List<Reservation> reservations = reservationRepository.findAllWithFilter(o);
        return reservations.stream().map(ReservationMappingHelper::map).toList();
    }

    @Override
    public ReservationResponseDTO findOne(String id) {
        return reservationRepository
                .findById(id)
                .map(ReservationMappingHelper::map)
                .orElseThrow(() -> new ReservationNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.RESERVATION_NOT_FOUND));
    }


    @Override
    public ReservationInitResponseDTO initReservation(ReservationInitRequestDTO o) {
        String reservationId = ReservationUtils.generateReservationIds();

        //        Create essential value to save on redis
        Map<String, String> initialDate = Map.of(
                "reservationId", reservationId,
                "userId", o.getUserId().toString(),
                "showtimeId", o.getShowtimeId().toString(),
                "theaterId", o.getTheaterId().toString(),
                "status", ReservationStatus.CREATED.toString(),
                "seats", "[]");

        //        Create expired time on redis key
        redisService.saveReservation(reservationId, initialDate, HOLD_TIMEOUT);
        log.info("Reservation initialize, Id: {}, user Id: {}, showtime Id: {}, theater Id: {} , ttl: {}", reservationId, o.getUserId(), o.getShowtimeId(), o.getTheaterId(), HOLD_TIMEOUT.toSeconds());

        return ReservationInitResponseDTO.builder()
                .reservationId(reservationId)
                .showtimeId(o.getShowtimeId())
                .ttl(HOLD_TIMEOUT.toSeconds())
                .expiredAt(Instant.now().plusSeconds(HOLD_TIMEOUT.toSeconds()))
                .build();
    }

    public void holdReservationSeats(ReservationHoldSeatRequestDTO reservation) {
        Long userId = reservation.getUserId();
        String reservationId = reservation.getReservationId();
        Long showtimeId = reservation.getShowtimeId();
        List<Long> seatIds = reservation.getSeatIds();
        //        Take reservationId and userId on redis
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);
        //        Define locks to update seat
        List<String> successfulLocks = new ArrayList<>();
        List<SeatLockFailure> lockFailures = new ArrayList<>();
        try {
            Map<Object, Object> reservationData = redisTemplate.opsForHash().entries(redisKey);
            if (reservationData.isEmpty()) {
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

            //        Validate showtime
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
            if (showtime.getAvailableSeats() < seatIds.size()) {
                throw new IllegalStateException(String.format("Not enough available seats. Request: %d, Available: %d", seatIds.size(), showtime.getAvailableSeats()));
            }


            Set<Long> oldSeatIds = new HashSet<>();
            String currentSeatsJson = (String) reservationData.get("seats");
            if (currentSeatsJson != null && !currentSeatsJson.isEmpty()) {
                oldSeatIds = new HashSet<>(mapper.readValue(currentSeatsJson, new TypeReference<List<Long>>() {
                }));
            }

            for (Long oldSeatId : oldSeatIds) {
                if (!seatIds.contains(oldSeatId)) {
                    redisTemplate.delete(RedisKeyHelper.seatLockKey(oldSeatId));
                }
            }


            //        Load seats from DB and validate
            List<Seat> seats = seatRepository.findAllById(seatIds);
            validateSeatsForReservation(seats, showtimeId, seatIds);


            //        Check seat in showtime was booked in redis
            for (Long seatId : seatIds) {
                String lockKey = RedisKeyHelper.seatLockKey(seatId);
                //            Check seat lock in redis
                Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, redisKey, HOLD_TIMEOUT);
                //            Get owner of this seat
                String existingOwner = (String) redisTemplate.opsForValue().get(lockKey);
                if (lockAcquired != null && lockAcquired) {
                    successfulLocks.add(lockKey);
                    log.debug("Successfully locked seat {} for reservation {}", seatId, reservationId);

                    //                Check the right owner
                } else {
                    if (redisKey.equals(existingOwner)) {
                        redisTemplate.expire(lockKey, HOLD_TIMEOUT);
                        successfulLocks.add(lockKey);
                        log.debug("Refreshed existing lock for seat {} in reservation {}", seatId, reservationId);
                    } else {
                        //                Check if the seat was hold by another owner
                        Seat seat = seats.stream().filter(s -> s
                                        .getId()
                                        .equals(seatId))
                                .findFirst()
                                .orElse(null);
                        String seatNumber = seat != null ? seat.getSeatNumber() : seatId.toString();
                        lockFailures.add(new SeatLockFailure(seatId, seatNumber, existingOwner));
                        log.warn("Seat {} ({}) is hold by reservation {}", seatId, seatNumber, existingOwner);
                    }
                }

            }

            //        If one of the list fail, return this exception
            if (!lockFailures.isEmpty()) {
                log.warn("Failed to lock {} for seats for reservation {}. Rolling back {} successful locks", lockFailures.size(), reservationId, successfulLocks.size());

                rollbackLock(successfulLocks, redisKey);

                String failedSeatMessage = lockFailures
                        .stream()
                        .map(f -> f.seatNumber + " (hold by " + f.ownerReservationId + ")")
                        .collect(Collectors.joining((", ")));
                throw new SeatUnavailableException(HttpStatus.CONFLICT, "Cannot hold seats. The following seats are currently hold by other users: " + failedSeatMessage);
            }


            //        Update seat in redis
            String seatsJson = mapper.writeValueAsString(seatIds);
            redisTemplate.opsForHash().put(redisKey, "seats", seatsJson);
            redisTemplate.expire(redisKey, HOLD_TIMEOUT);


            Map<String, Object> event = Map.of(
                    "event", "SEAT_HOLD",
                    "showtimeId", showtimeId,
                    "seatIds", seatIds,
                    "userId", userId
            );

            //            Send notification via pub sub
            redisService.publishSeatHold(showtimeId, event);
            log.info("Successfully hold {} seats with user id {} for reservation {}: {} ", seatIds.size(), userId, reservationId, seatIds);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize seat IDs for reservation {}", reservationId, ex);
            rollbackLock(successfulLocks, redisKey);
            throw new RedisStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store seat information. Please try again");
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ReservationResponseDTO confirmReservation(ReservationRequestDTO request) {


        log.info("Confirming reservation: {}", request.getReservationId());

        //        Get reservation id from redis
        String reservationId = request.getReservationId();
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);

        Reservation dto = getFromRedis(reservationId);
        if (!dto.getUser().getId().equals(request.getUserId())) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permission to confirm this reservation");
        }

        //      Load seats from DB
        List<Long> seatIds = dto
                .getSeats()
                .stream()
                .map(Seat::getId)
                .toList();
        if (seatIds.isEmpty()) {
            throw new IllegalStateException(Message.Exception.NO_SEAT_SELECTED);
        }

        List<Long> expiredLocks = new ArrayList<>();
        List<Long> stolenLocks = new ArrayList<>();

        for (Long seatId : seatIds) {
            String lockKey = RedisKeyHelper.seatLockKey(seatId);
            String lockOwner = (String) redisTemplate.opsForValue().get(lockKey);

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


        //        Take seat from request
        List<Seat> seatEntities = seatRepository.findByIdWithLock(seatIds);
        //        Create reservation to save
        Showtime showtime = showtimeRepository
                .findById(request.getShowtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with id : " + request.getShowtimeId()));
        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User now found with id :" + request.getUserId()));
        if (seatEntities.size() != seatIds.size()) {
            log.error("Expected {} seats but found {} for reservation {}", seatIds.size(), seatEntities.size(), reservationId);
            throw new IllegalStateException("Some seats not found in database");
        }

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
                .isDeleted(false)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        int oldAvailableSeats = showtime.getAvailableSeats();
        int newAvailableSeats = oldAvailableSeats - seatEntities.size();
        if (newAvailableSeats < 0) {
            log.error("Showtime {} available seats would become negative: {}, Current: {}, Booking: {}", showtime.getId(), newAvailableSeats, showtime.getAvailableSeats(), seatEntities.size());
            throw new IllegalStateException("Showtime available seats calculation error");
        }

        for (Seat seat : seatEntities) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setReservation(savedReservation);
        }

        seatRepository.saveAll(seatEntities);
        log.info("Updated {} seats to BOOKED status", seatEntities.size());

        //      Update Unavailable seats count in showtime
        showtime.setAvailableSeats(newAvailableSeats);
        showtimeRepository.save(showtime);

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

        log.info("Updated showtime {} available seats from {} to {}", showtime.getId(), showtime.getAvailableSeats(), newAvailableSeats);


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
        List<Long> seatIds = reservationHold
                .getSeats()
                .stream()
                .map(Seat::getId)
                .toList();
        if (seatIds.isEmpty()) {
            log.warn("No seats to release for reservation {}", reservationId);
            return;
        }


        List<String> lockKeys = new ArrayList<>();
        for (Long seatId : seatIds) {
            String lockKey = RedisKeyHelper.seatLockKey(seatId);
            String lockOwner = (String) redisTemplate.opsForValue().get(lockKey);

            if (redisKey.equals(lockOwner)) {
                lockKeys.add(lockKey);
                redisTemplate.delete(lockKey);
                log.warn("Release lock for seat {} in", seatId);
            } else {
                log.warn("Seat {} lock not owned by this reservation, skip", seatId);
            }
        }

        log.info("ALl Redis locks verified for reservation {}", reservationId);


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
                .isDeleted(false)
                .build();

        reservationRepository.save(reservation);
        log.info("Updated reservation {} status to FAILED", reservationId);

        redisTemplate.delete(redisKey);
        log.info("Remove reservation hold key {}", redisKey);

        Map<String, Object> event = Map.of(
                "event", "SEAT_RELEASE",
                "showtimeId", showtime.getId(),
                "seatIds", seatIds,
                "userId", userId
        );

        //            Send notification via pub sub
//        redisService.publishSeatHold(showtime.getId(), event);
        log.info("Published seat release event for showtime {}: {}", showtime.getId(), seatIds);

        log.info("Cancelled reservation hold {} successfully", reservationId);
    }

    //    Get data from redis
    private Reservation getFromRedis(String reservationId) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);
        if (data.isEmpty()) {
            throw new ReservationExpiredException(HttpStatus.NOT_FOUND, Message.Exception.RESERVATION_EXPIRED);
        }

        Reservation dto = new Reservation();
        dto.setId(reservationId);

        Long userId = safeParseLong((String) data.get("userId"), "userId");
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Message.Exception.USER_NOT_FOUND));

        dto.setUser(user);
        dto.setReservationStatus(ReservationStatus.valueOf((String) data.get("status")));
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

    private static class SeatLockFailure {
        final Long seatId;
        final String seatNumber;
        final String ownerReservationId;

        public SeatLockFailure(Long seatId, String seatNumber, String ownerReservationId) {
            this.seatId = seatId;
            this.seatNumber = seatNumber;
            this.ownerReservationId = ownerReservationId;
        }
    }

    //        Clean up Redis
    private void cleanupRedisLocks(String redisKey, String reservationId, List<Seat> seats) {
        int deletedLocks = 0;

        for (Seat seat : seats) {
            String lockKey = RedisKeyHelper.seatLockKey(seat.getId());
            String currentOwner = (String) redisTemplate.opsForValue().get(lockKey);

            if (redisKey.equals(currentOwner)) {
                Boolean deleted = redisTemplate.delete(lockKey);
                if (deleted) {
                    deletedLocks++;
                    log.debug("Deleted lock for seat {}", seat.getId());
                }
            } else {
                log.warn("Lock for seat {} has unexpected owner: {}. Expected: {}", seat.getId(), currentOwner, lockKey);
            }
        }

        Boolean reservationDeleted = redisTemplate.delete(reservationId);
        if (reservationDeleted) {
            deletedLocks++;
        }
        log.info("Cleaned up Redis: {} seat locks deleted, reservation key deleted: {}", deletedLocks, reservationDeleted);
    }

    private Long safeParseLong(String value, String fieldName) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("Invalid {} format in Redis: {}", fieldName, value);
            throw new RedisStorageException(HttpStatus.BAD_REQUEST, "Invalid " + fieldName + " format in Redis");
        }
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

    private void rollbackLock(List<String> lockKeys, String expectedOwner) {
        if (lockKeys.isEmpty()) {
            return;
        }

        log.info("Rolling back {} seat lock for reservation {}", lockKeys.size(), expectedOwner);

        int rollbackCount = 0;
        for (String lockKey : lockKeys) {
            try {
                String currentOwner = (String) redisTemplate.opsForValue().get(lockKey);

                if (expectedOwner.equals(currentOwner)) {
                    redisTemplate.delete(lockKey);
                    rollbackCount++;
                } else {
                    log.warn("Lock {} ownership changed during rollback. Expected: {}, Current: {}", lockKey, expectedOwner, currentOwner);
                }

            } catch (Exception ex) {
                log.error("Failed to rollback lock {}", lockKey, ex);
            }
        }
        log.info("Successfully rolled back {}/{} locks", rollbackCount, lockKeys.size());
    }

}
