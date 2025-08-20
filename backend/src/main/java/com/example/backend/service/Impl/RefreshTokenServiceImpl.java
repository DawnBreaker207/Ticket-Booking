package com.example.backend.service.Impl;

import com.example.backend.exception.wrapper.RefreshTokenExpiredException;
import com.example.backend.model.RefreshToken;
import com.example.backend.repository.Impl.RefreshTokenRepositoryImpl;
import com.example.backend.repository.Impl.UserRepositoryImpl;
import com.example.backend.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${dawn.app.jwtRefreshExpirationsMs}")
    private Long refreshTokenDurations;

    private final RefreshTokenRepositoryImpl refreshTokenRepository;
    private final UserRepositoryImpl userRepository;

    public RefreshTokenServiceImpl(
            UserRepositoryImpl userRepository,
            RefreshTokenRepositoryImpl refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository
                .findOne(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurations));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token.getToken());
            throw new RefreshTokenExpiredException(token.getToken() + " Refresh token was expired, Please make a new signin request");
        }
        return token;
    }

    @Override
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userRepository
                .findOne(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + userId)));
    }
}
