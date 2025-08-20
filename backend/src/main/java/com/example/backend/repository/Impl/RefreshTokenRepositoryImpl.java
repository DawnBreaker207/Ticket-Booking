package com.example.backend.repository.Impl;

import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.model.RefreshToken;
import com.example.backend.model.User;
import com.example.backend.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final DataSource datasource;

    public RefreshTokenRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public RefreshToken save(RefreshToken retoken) {
        String sql = "INSERT INTO refresh_token (token , user_id, expiry_date) VALUES  (? , ? ,? )";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setString(1, retoken.getToken());
            pre.setLong(2, retoken.getUser().getId());
            pre.setTimestamp(3, Timestamp.from(retoken.getExpiryDate()));
            pre.executeUpdate();
            try (var rs = pre.getGeneratedKeys();) {
                if (rs.next()) {
                    retoken.setId(rs.getInt(1));
                }

            }
            return retoken;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String sql = """
                SELECT r.id as r_id, r.token, r.expiry_date, u.id AS u_id, u.username, u.email 
                FROM refresh_token r 
                JOIN users u ON r.user_id = u.id
                WHERE token = ?""";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, token);
            try (var rs = pre.executeQuery()) {
                RefreshToken retoken = new RefreshToken();
                User user = new User();
                if (rs.next()) {
                    user.setId(rs.getLong("u_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));


                    retoken.setId(rs.getInt("r_id"));
                    retoken.setToken(rs.getString("token"));
                    retoken.setExpiryDate(rs.getTimestamp("expiry_date").toInstant());
                    retoken.setUser(user);
                    return Optional.of(retoken);
                }
                return Optional.empty();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String token) {
        String sql = "DELETE FROM refresh_token WHERE token = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, token);

            int rows = pre.executeUpdate();

            if (rows == 0) {
                throw new MovieNotFoundException(HttpStatus.NOT_FOUND, "Not match found with token " + token);
            }
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteByUser(User user) {
        String sql = "DELETE FROM refresh_token WHERE user_id = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setLong(1, user.getId());

            int rows = pre.executeUpdate();

            if (rows == 0) {
                throw new MovieNotFoundException(HttpStatus.NOT_FOUND, "Not match found with user id " + user.getId());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
