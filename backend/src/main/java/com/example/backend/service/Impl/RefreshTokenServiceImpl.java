package com.example.backend.service.Impl;

import com.example.backend.constant.Message;
import com.example.backend.exception.wrapper.RefreshTokenExpiredException;
import com.example.backend.exception.wrapper.RefreshTokenNotFoundException;
import com.example.backend.model.RefreshToken;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${dawn.app.jwtRefreshExpirationsMs}")
    private Long refreshTokenDurations;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken
                .builder()
                .user(userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException(Message.Exception.USER_NOT_FOUND)))
                .expiryDate(Instant.now().plusMillis(refreshTokenDurations))
                .token(UUID.randomUUID().toString())
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken;

    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional
                .ofNullable(refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() -> new RefreshTokenNotFoundException(Message.Exception.REFRESH_TOKEN_NOT_FOUND)));
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(token.getToken());
            throw new RefreshTokenExpiredException(Message.Exception.REFRESH_TOKEN_EXPIRED);
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(Message.Exception.USER_NOT_FOUND)));
    }
}
