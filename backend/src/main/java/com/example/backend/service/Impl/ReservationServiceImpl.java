package com.example.backend.service.Impl;

import com.example.backend.constant.ReservationStatus;
import com.example.backend.constant.SeatStatus;
import com.example.backend.dto.request.ReservationFilterDTO;
import com.example.backend.dto.request.ReservationHoldSeatRequestDTO;
import com.example.backend.dto.request.ReservationInitRequestDTO;
import com.example.backend.dto.request.ReservationRequestDTO;
import com.example.backend.dto.response.ReservationResponseDTO;
import com.example.backend.exception.wrapper.*;
import com.example.backend.helper.RedisKeyHelper;
import com.example.backend.helper.ReservationMappingHelper;
import com.example.backend.model.Reservation;
import com.example.backend.model.Seat;
import com.example.backend.model.Showtime;
import com.example.backend.model.User;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.SeatRepository;
import com.example.backend.repository.ShowtimeRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.NotificationService;
import com.example.backend.service.ReservationService;
import com.example.backend.util.ReservationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final SimpMessagingTemplate messagingTemplate;

    private final ShowtimeRepository showtimeRepository;

    private final NotificationService notificationService;

    @Override
    public List<ReservationResponseDTO> findAll(ReservationFilterDTO o) {
        List<Reservation> reservations = reservationRepository.findAllWithFilter(o);
        return reservations.stream().map(ReservationMappingHelper::map).toList();
    }

    @Override
    public Reservation findOne(String id) {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

//    @Scheduled(fixedRate = 5000)
//    public void getCountdown() {
//        log.info("Running getCountdown schedule task");
//        Set<String> keys = redisTemplate.keys("reservationId:*");
//        log.info("Getting countdown keys {}", keys);
//        if (keys.isEmpty()) return;
//        for (String redisKey : keys) {
//            Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
//            log.info("Getting count down task for {}", redisKey);
//            if (ttl > 0) {
//                String reservationId = redisKey.substring(redisKey.lastIndexOf(":") + 1);
//                messagingTemplate.convertAndSend("/topic/reservation/" + reservationId, Map.of(
//                        "event", "TTL_SYNC",
//                        "reservationId", reservationId,
//                        "ttl", ttl
//                ));
//                log.info("Sending TTL_SYNC for reservation {} with TTL {}", reservationId, ttl);
//            }
//        }
//    }

    @Override
    public String initReservation(ReservationInitRequestDTO o) {
        validateReservationInitRequest(o);

        String reservationId = o.getReservationId();
        String redisKey;
//        Check if reservation id existed
        if (reservationId != null && redisTemplate.hasKey(RedisKeyHelper.reservationHoldKey(reservationId))) {
            redisKey = RedisKeyHelper.reservationHoldKey(reservationId);
            redisTemplate.expire(redisKey, HOLD_TIMEOUT);

            sendTtlSyncMessage(reservationId, o.getUserId(), o.getShowtimeId(), o.getTheaterId(), HOLD_TIMEOUT.toSeconds());
            return reservationId;
        }

//        Create new reservation id
        reservationId = ReservationUtils.generateReservationIds();
        redisKey = RedisKeyHelper.reservationHoldKey(reservationId);


//        Create essential value to save on redis
        Map<String, String> reservationData = Map.of(
                "reservationId", reservationId,
                "userId", o.getUserId().toString(),
                "showtimeId", o.getShowtimeId().toString(),
                "theaterId", o.getTheaterId().toString(),
                "status", ReservationStatus.CREATED.toString(),
                "seats", "[]");

        redisTemplate.opsForHash().putAll(redisKey, reservationData);
//        Create expired time on redis key
        redisTemplate.expire(redisKey, HOLD_TIMEOUT);

//        Send this information via websocket
        sendTtlSyncMessage(reservationId, o.getUserId(), o.getShowtimeId(), o.getTheaterId(), HOLD_TIMEOUT.toSeconds());
        return reservationId;
    }

    public void holdSeats(ReservationHoldSeatRequestDTO reservation) {

        validateHoldSeatRequest(reservation);

        Long userId = reservation.getUserId();
        String reservationId = reservation.getReservationId();
        Long showtimeId = reservation.getShowtimeId();
        List<Long> seatIds = reservation.getSeatIds();


//        Take reservationId and userId on redis
        String redisKey = RedisKeyHelper.reservationHoldKey(reservationId);
        Map<Object, Object> reservationData = redisTemplate.opsForHash().entries(redisKey);

        String userIdStr = (String) reservationData.get("userId");
        String reservationIdStr = (String) reservationData.get("reservationId");

//        Check and compare value valid
        if (userIdStr == null || reservationIdStr == null) {
            throw new ReservationNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid reservation data");
        }

        if (!reservationIdStr.equals(reservationId)) {
            throw new ReservationNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid reservation data");
        }

        Long userIdRedis = Long.parseLong(userIdStr);

        if (!userId.equals(userIdRedis)) {
            throw new ForbiddenPermissionException(HttpStatus.FORBIDDEN, "You don't have permission !");
        }

//        Check user existed
        userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

//        Validate showtime
        Showtime showtime = showtimeRepository
                .findById(showtimeId)
                .orElseThrow(() -> new TheaterNotFoundException("Showtime not found with id: " + showtimeId));

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

//        Load seats from DB and validate
        List<Seat> seats = seatRepository.findAllById(seatIds);
        validateSeatsForReservation(seats, showtimeId, seatIds);


        List<String> successfulLocks = new ArrayList<>();
        List<SeatLockFailure> lockFailures = new ArrayList<>();

//        Check seat in showtime was booked in redis
        for (Long seatId : seatIds) {
            String lockKey = RedisKeyHelper.seatLockKey(seatId);
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, redisKey, HOLD_TIMEOUT);

            if (lockAcquired != null && lockAcquired) {
                successfulLocks.add(lockKey);
                log.debug("Successfully locked seat {} for reservation {}", seatId, reservationId);

            } else {
                String existingOwner = (String) redisTemplate.opsForValue().get(lockKey);

                if (redisKey.equals(existingOwner)) {
                    redisTemplate.expire(lockKey, HOLD_TIMEOUT);
                    successfulLocks.add(lockKey);
                    log.debug("Refreshed existing lock for seat {} in reservation {}", seatId, reservationId);
                } else {
                    Seat seat = seats.stream().filter(s -> s.getId().equals(seatId)).findFirst().orElse(null);
                    String seatNumber = seat != null ? seat.getSeatNumber() : seatId.toString();

                    lockFailures.add(new SeatLockFailure(seatId, seatNumber, existingOwner));
                    log.warn("Seat {} ({}) is held by reservation {}", seatId, seatNumber, existingOwner);
                }
            }

        }

        if (!lockFailures.isEmpty()) {
            log.warn("Failed to lock {} for seats for reservation {}. Rolling back {} successful locks", lockFailures.size(), reservationId, successfulLocks.size());

            rollbackLock(successfulLocks, redisKey);

            String failedSeatMessage = lockFailures
                    .stream()
                    .map(f -> f.seatNumber + " (held by " + f.ownerReservationId + ")")
                    .collect(Collectors.joining((", ")));
            throw new SeatUnavailableException(HttpStatus.CONFLICT, "Cannot hold seats. The following seats are currently held by other users: " + failedSeatMessage);
        }

        try {
//        Update seat in redis
            String seatsJson = new ObjectMapper().writeValueAsString(seatIds);

            redisTemplate.opsForHash().put(redisKey, "seats", seatsJson);

            redisTemplate.expire(redisKey, HOLD_TIMEOUT);

            log.info("Successfully held {} seats for reservation {}: {}", seatIds.size(), reservationId, seatIds);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize seat IDs for reservation {}", reservationId, ex);
            rollbackLock(successfulLocks, redisKey);
            throw new RedisStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store seat information. Please try again");
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ReservationResponseDTO confirm(ReservationRequestDTO request) {


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
            throw new IllegalStateException("No seats selected in this reservation");
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
                .orElseThrow(() -> new TheaterNotFoundException("Showtime not found with id : " + request.getShowtimeId()));
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


        notificationService.sendEmail(user.getEmail(), user.getUsername(), reservationId);

        cleanupRedisLocks(redisKey, reservationId, seatEntities);

        log.info("Updated showtime {} available seats from {} to {}", showtime.getId(), showtime.getAvailableSeats(), newAvailableSeats);


        log.info("Successfully confirmed reservation: {} with {} seats", reservation.getId(), seatEntities.size());
        return ReservationMappingHelper.map(savedReservation);
    }


    //    Get data from redis
    private Reservation getFromRedis(String reservationId) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);
        if (data.isEmpty()) {
            throw new ReservationExpiredException(HttpStatus.NOT_FOUND, "Reservation expired or not existed");
        }

        Reservation dto = new Reservation();
        dto.setId(reservationId);

        Long userId = safeParseLong((String) data.get("userId"), "userId");
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        dto.setUser(user);
        dto.setReservationStatus(ReservationStatus.valueOf((String) data.get("status")));
        Long showtimeId = safeParseLong((String) data.get("showtimeId"), "showtimeId");
        Showtime showtime = showtimeRepository
                .findById(showtimeId)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with id " + showtimeId));
        dto.setShowtime(showtime);

        try {
            String seatJson = (String) data.get("seats");
            List<Long> seatIds = new ObjectMapper().readValue(seatJson, new TypeReference<>() {
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

    private void validateReservationInitRequest(ReservationInitRequestDTO request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User Id cannot be empty");
        }
        if (request.getShowtimeId() == null) {
            throw new IllegalArgumentException("Showtime Id cannot be empty");
        }
        if (request.getTheaterId() == null) {
            throw new IllegalArgumentException("Theater Id cannot be empty");
        }
    }

    private void validateHoldSeatRequest(ReservationHoldSeatRequestDTO request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User Id cannot be empty");
        }
        if (request.getReservationId() == null || request.getReservationId().trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation Id cannot be empty");
        }
        if (request.getShowtimeId() == null) {
            throw new IllegalArgumentException("Showtime Id cannot be empty");
        }
        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new IllegalArgumentException("At least one seat must be selected");
        }
    }

    private void sendTtlSyncMessage(String reservationId, Long userId, Long showtimeId, Long theaterId, long ttlSeconds) {
        messagingTemplate.convertAndSend("/topic/reservation/" + reservationId, Map.of(
                "event", "TTL_SYNC",
                "reservationId", reservationId,
                "userId", userId.toString(),
                "showtimeId", showtimeId.toString(),
                "theaterId", theaterId.toString(),
                "ttl", ttlSeconds));
        log.info("Sending TTL_SYNC for reservation {} with TTL {}", reservationId, ttlSeconds);
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
            throw new SeatUnavailableException("Seat not found with id: " + notFoundSeatIds);
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
            throw new IllegalArgumentException("Seats " + wrongSeatNumbers + " do not belong to the requested showtime");
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
            throw new SeatUnavailableException(HttpStatus.CONFLICT, "Seats you choose already booked " + bookedSeatNumbers);
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
            throw new SeatUnavailableException(HttpStatus.CONFLICT, "Seat no longer available " + String.join(", ", unavailableSeats));
        }

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
