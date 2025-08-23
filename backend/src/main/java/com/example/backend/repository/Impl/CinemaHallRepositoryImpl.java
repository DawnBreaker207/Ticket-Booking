package com.example.backend.repository.Impl;

import com.example.backend.constant.SeatStatus;
import com.example.backend.exception.wrapper.CinemaHallNotFoundException;
import com.example.backend.model.CinemaHall;
import com.example.backend.model.Movie;
import com.example.backend.model.Seat;
import com.example.backend.repository.CinemaHallRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CinemaHallRepositoryImpl implements CinemaHallRepository {
    private final DataSource datasource;

    public CinemaHallRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public List<CinemaHall> findAll() {
        String sql = """
                SELECT 
                ch.id AS hall_id, ch.movie_id AS hall_movie_id, ch.movie_session, ch.created_at, ch.updated_at,
                s.id AS seat_id, s.cinema_hall_id, s.seat_number, s.status, s.price,
                m.id AS movie_id, m.poster, m.title, m.overview, m.duration, m.genres, m.release_date
                FROM cinema_hall AS ch 
                INNER JOIN seat AS s ON ch.id = s.cinema_hall_id
                INNER JOIN movie AS m ON ch.movie_id = m.id
                """;
        Map<Long, CinemaHall> halls = new HashMap<>();
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql); var rs = pre.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie();
                List<String> genres = Arrays.asList(rs.getString("genres").split(","));
                movie.setId(rs.getLong("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setPoster(rs.getString("poster"));
                movie.setOverview(rs.getString("overview"));
                movie.setReleaseDate(rs.getDate("release_date"));
                movie.setGenres(genres);

                Long hallId = rs.getLong("hall_id");
                var hall = halls.get(hallId);
                if (hall == null) {
                    hall = new CinemaHall();
                    hall.setId(hallId);
                    hall.setMovie(movie);
                    hall.setMovieSession(rs.getTimestamp("movie_session"));
                    hall.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    hall.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
                    hall.setSeats(new ArrayList<>());
                    halls.put(hallId, hall);
                }
                Seat seat = new Seat();
                seat.setId(rs.getInt("seat_id"));
                seat.setCinemaHallId(rs.getInt("cinema_hall_id"));
                seat.setPrice(rs.getBigDecimal("price"));
                seat.setSeatStatus(SeatStatus.valueOf(rs.getString("status")));
                seat.setSeatNumber(rs.getString("seat_number"));
                hall.getSeats().add(seat);
            }

            return new ArrayList<>(halls.values());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<CinemaHall> findOne(Long id) {
        String sql = """
                SELECT 
                ch.id AS hall_id, ch.movie_id AS hall_movie_id, ch.movie_session, ch.created_at, ch.updated_at,
                s.id AS seat_id, s.cinema_hall_id, s.seat_number, s.status, s.price,
                m.id AS movie_id, m.poster, m.title, m.overview, m.duration, m.genres, m.release_date
                FROM cinema_hall AS ch 
                INNER JOIN seat s ON ch.id = s.cinema_hall_id 
                INNER JOIN movie AS m ON ch.movie_id = m.id
                WHERE ch.id = ? 
                """;
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql);) {

            pre.setLong(1, id);

            try (var rs = pre.executeQuery()) {
                CinemaHall hall = null;
                List<Seat> seats = new ArrayList<>();

                while (rs.next()) {
                    Movie movie = new Movie();
                    List<String> genres = Arrays.asList(rs.getString("genres").split(","));
                    movie.setId(rs.getLong("movie_id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setPoster(rs.getString("poster"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setReleaseDate(rs.getDate("release_date"));
                    movie.setGenres(genres);

                    if (hall == null) {
                        hall = new CinemaHall();
                        hall.setId(rs.getLong("hall_id"));
                        hall.setMovie(movie);
                        hall.setMovieSession(rs.getTimestamp("movie_session"));
                        hall.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                        hall.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
                    }

                    int seatId = rs.getInt("seat_id");
                    if (!rs.wasNull()) {
                        Seat seat = new Seat();
                        seat.setId(seatId);
                        seat.setCinemaHallId(rs.getInt("cinema_hall_id"));
                        seat.setSeatNumber(rs.getString("seat_number"));
                        seat.setPrice(rs.getBigDecimal("price"));
                        seat.setSeatStatus(SeatStatus.valueOf(rs.getString("status")));
                        seats.add(seat);
                    }

                }
                if (hall != null) {
                    hall.setSeats(seats);
                    return Optional.of(hall);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<CinemaHall> findByMovieIdAndMovieSession(Long movieId, String movieSession) {
        String sql = """
                SELECT 
                ch.id AS hall_id, ch.movie_id AS hall_movie_id, ch.movie_session, ch.created_at, ch.updated_at,
                s.id AS seat_id, s.cinema_hall_id, s.seat_number, s.status, s.price,
                m.id AS movie_id, m.poster, m.title, m.overview, m.duration, m.genres, m.release_date    
                FROM cinema_hall AS ch 
                INNER JOIN seat s ON ch.id = s.cinema_hall_id 
                INNER JOIN movie AS m ON ch.movie_id = m.id
                WHERE hall_movie_id = ? 
                AND movie_session = ?
                """;
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setLong(1, movieId);
            pre.setString(2, movieSession);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    List<String> genres = Arrays.asList(rs.getString("genres").split(","));
                    movie.setId(rs.getLong("movie_id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setPoster(rs.getString("poster"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setReleaseDate(rs.getDate("release_date"));

                    movie.setGenres(genres);
                    CinemaHall hall = new CinemaHall();
                    hall.setId(rs.getLong("hall_id"));
                    hall.setMovie(movie);
                    hall.setMovieSession(rs.getTimestamp("movie_session"));
                    hall.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    hall.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());

                    return Optional.of(hall);
                }
            }
            return Optional.empty();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

//    For admin

    @Override
    public CinemaHall save(CinemaHall cinema) {
        String insertHall = "INSERT INTO cinema_hall (movie_id, movie_session) VALUES (? ,?)";
        String insertSeat = "INSERT INTO  seat (seat_number, status, cinema_hall_id, price) VALUES (? ,'AVAILABLE' , ? , ?)";
        try (var conn = datasource.getConnection();
             var pre = conn.prepareStatement(insertHall, Statement.RETURN_GENERATED_KEYS);) {

            pre.setLong(1, cinema.getMovie().getId());
            pre.setTimestamp(2, Timestamp.from(cinema.getMovieSession().toInstant()));
            pre.executeUpdate();
            try (var result = pre.getGeneratedKeys()) {
                if (result.next()) {
                    cinema.setId(result.getLong(1));
                } else {
                    throw new SQLException("Can't not get id cinema hall");
                }
            }
//	    Insert seats
            try (var seat = conn.prepareStatement(insertSeat)) {
                for (char row = 'A'; row <= 'E'; row++) {
                    for (int i = 1; i <= 10; i++) {
                        seat.setString(1, row + String.valueOf(i));
                        seat.setLong(2, cinema.getId());
                        seat.setBigDecimal(3, BigDecimal.valueOf(50000));
                        seat.addBatch();
                    }
                }

                seat.executeBatch();
            }
            return findOne(cinema.getId())
                    .orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Cinema hall not found after create"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CinemaHall update(CinemaHall h) {
        String sql = "UPDATE cinema_hall SET movie_id = ? , movie_session = ? WHERE id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {

            pre.setLong(1, h.getMovie().getId());
            pre.setTimestamp(2, Timestamp.from(h.getMovieSession().toInstant()));
            pre.setLong(3, h.getId());
            int affected = pre.executeUpdate();

            if (affected == 0) {
                throw new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "No cinema hall updated");
            }

            return findOne(h.getId())
                    .orElseThrow(() -> new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Cinema hall not found after update"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Long id) {
        String seats = "DELETE FROM cinema_hall ch WHERE ch.id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(seats);) {
            conn.setAutoCommit(false);

            pre.setLong(1, id);
            int rows = pre.executeUpdate();

            if (rows == 0) {
                conn.rollback();
                throw new CinemaHallNotFoundException(HttpStatus.NOT_FOUND, "Not match found with id " + id);
            }
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

//   For Client

    @Override
    public void updateSeatStatus(List<String> seatIds, SeatStatus seatStatus) {
        if (seatIds == null || seatIds.isEmpty())
            return;
        String placeHolders = seatIds.stream().map(id -> "?").collect(Collectors.joining(", "));
        String sql = "UPDATE seat SET status = ? WHERE seat_number IN (" + placeHolders + ")";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, seatStatus.name());
            for (int i = 0; i < seatIds.size(); i++) {
                pre.setString(i + 2, seatIds.get(i));
            }

            int affected = pre.executeUpdate();
            if (affected == 0) {
                throw new SQLException("No seat updated");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

}
