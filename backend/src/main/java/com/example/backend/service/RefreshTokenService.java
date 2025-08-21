package com.example.backend.service;

import com.example.backend.model.RefreshToken;
import com.example.backend.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteByUserId(Long userId);

}
