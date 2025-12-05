package com.dawn.backend.service.Impl;

import com.dawn.backend.constant.Message;
import com.dawn.backend.exception.wrapper.RefreshTokenExpiredException;
import com.dawn.backend.exception.wrapper.RefreshTokenNotFoundException;
import com.dawn.backend.exception.wrapper.ResourceNotFoundException;
import com.dawn.backend.model.RefreshToken;
import com.dawn.backend.model.User;
import com.dawn.backend.repository.RefreshTokenRepository;
import com.dawn.backend.repository.UserRepository;
import com.dawn.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.USER_NOT_FOUND));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurations))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(refreshToken);
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
        userRepository
                .findById(userId)
                .ifPresent(refreshTokenRepository::deleteByUser);
    }
}
