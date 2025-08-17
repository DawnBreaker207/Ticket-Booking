package com.example.backend.repository.Impl;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.example.backend.dto.MovieDTO;
import org.springframework.stereotype.Repository;

import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.model.Movie;
import com.example.backend.repository.MovieRepository;

@Repository
public class MovieRepositoryImpl implements MovieRepository {

    private final DataSource datasource;

    public MovieRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public List<Movie> findAll(MovieDTO m) {
        StringBuilder sql = new StringBuilder("SELECT * FROM movie WHERE 1=1");
        List<Movie> movies = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (m != null) {

            if (m.getTitle() != null) {
                sql.append(" AND title LIKE ?");
                params.add("%" + m.getTitle() + "%");
            }

            if (m.getDuration() != null) {
                sql.append(" AND duration = ?");
                params.add(m.getDuration());
            }

            if (m.getReleaseDate() != null) {
                sql.append(" AND releaseDate = ?");
                params.add(m.getReleaseDate());
            }
            sql.append(" ORDER BY id DESC");
        }

        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    var movie = new Movie();
                    List<String> genres = Arrays.asList(rs.getString("genre").split(","));

                    movie.setId(rs.getLong("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setImdbId(rs.getString("imdb_id"));
                    movie.setFilmId(rs.getString("film_id"));
                    movie.setDuration(rs.getInt("duration"));
                    movie.setPoster(rs.getString("poster"));
                    movie.setReleaseDate(rs.getDate("release_date"));
                    movie.setGenre(genres);

                    movies.add(movie);
                }
            }
            return movies;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Movie> findOne(Long id) {
        String sql = "SELECT * FROM movie WHERE id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setLong(1, id);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    List<String> genres = Arrays.asList(rs.getString("genre").split(","));
                    movie.setId(rs.getLong("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setImdbId(rs.getString("imdb_id"));
                    movie.setFilmId(rs.getString("film_id"));
                    movie.setDuration(rs.getInt("duration"));
                    movie.setPoster(rs.getString("poster"));
                    movie.setReleaseDate(rs.getDate("release_date"));
                    movie.setGenre(genres);
                    return Optional.of(movie);
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
    public Optional<Movie> findByMovieId(String id) {
        String sql = "SELECT * FROM movie WHERE film_id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    List<String> genres = Arrays.asList(rs.getString("genre").split(","));
                    movie.setId(rs.getLong("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setImdbId(rs.getString("imdb_id"));
                    movie.setFilmId(rs.getString("film_id"));
                    movie.setDuration(rs.getInt("duration"));
                    movie.setPoster(rs.getString("poster"));
                    movie.setReleaseDate(rs.getDate("release_date"));
                    movie.setGenre(genres);
                    return Optional.of(movie);
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
    public Movie save(Movie m) {
        String sql = "INSERT INTO movie (title, poster, overview, duration, genre, release_date, imdb_id, film_id) VALUES (?, ? ,? ,? ,? ,? ,? ,?)";
        try (var conn = datasource.getConnection();
             var pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setString(1, m.getTitle());
            pre.setString(2, m.getPoster());
            pre.setString(3, m.getOverview());
            pre.setInt(4, m.getDuration());
            pre.setString(5, String.join(",", m.getGenre()));
            pre.setDate(6, new java.sql.Date(m.getReleaseDate().getTime()));
            pre.setString(7, m.getImdbId());
            pre.setString(8, m.getFilmId());
            pre.executeUpdate();
            try (var rs = pre.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getLong(1));
                }
            }
            return m;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Movie update(Movie m) {
        String sql = "UPDATE movie SET title = ?, overview = ?, imdb_id = ?, duration = ?, poster = ?, release_date = ?, genre = ? WHERE id = ?";
        String update = "SELECT * FROM movie WHERE id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, m.getTitle());
            pre.setString(2, m.getOverview());
            pre.setString(3, m.getImdbId());
            pre.setInt(4, m.getDuration());
            pre.setString(5, m.getPoster());
            pre.setDate(6, new java.sql.Date(m.getReleaseDate().getTime()));
            pre.setString(7, String.join(",", m.getGenre()));
            pre.setLong(8, m.getId());
            pre.executeUpdate();
            try (var select = conn.prepareStatement(update)) {
                select.setLong(1, m.getId());
                try (var rs = select.executeQuery()) {

                    if (rs.next()) {
                        Movie movie = new Movie();
                        List<String> genres = Arrays.asList(rs.getString("genre").split(","));
                        movie.setId(rs.getLong("id"));
                        movie.setTitle(rs.getString("title"));
                        movie.setOverview(rs.getString("overview"));
                        movie.setImdbId(rs.getString("imdb_id"));
                        movie.setDuration(rs.getInt("duration"));
                        movie.setPoster(rs.getString("poster"));
                        movie.setReleaseDate(rs.getDate("release_date"));
                        movie.setFilmId(rs.getString(rs.getString("film_id")));
                        movie.setGenre(genres);

                        return movie;
                    } else {
                        throw new MovieNotFoundException("Not match found with id " + m.getId());
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM movie WHERE id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setLong(1, id);

            int rows = pre.executeUpdate();

            if (rows == 0) {
                throw new MovieNotFoundException("Not match found with id " + id);
            }
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
