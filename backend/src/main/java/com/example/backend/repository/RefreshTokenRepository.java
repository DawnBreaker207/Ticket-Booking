package com.example.backend.repository;

import com.example.backend.model.RefreshToken;
import com.example.backend.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends DAO<RefreshToken> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
