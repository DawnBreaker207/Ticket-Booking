package com.example.backend.service.Impl;

import com.example.backend.constant.Message;
import com.example.backend.constant.URole;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.response.JwtResponseDTO;
import com.example.backend.dto.response.TokenRefreshResponseDTO;
import com.example.backend.exception.wrapper.RefreshTokenExpiredException;
import com.example.backend.exception.wrapper.RefreshTokenNotFoundException;
import com.example.backend.exception.wrapper.UserEmailExistedException;
import com.example.backend.exception.wrapper.UsernameExistedException;
import com.example.backend.model.RefreshToken;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDetailsImpl;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.RefreshTokenService;
import com.example.backend.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jWTUtils;

    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void register(RegisterRequestDTO newUser) {

        userRepository
                .findByEmail(newUser.getEmail())
                .ifPresent(u -> {
                    throw new UserEmailExistedException(Message.Exception.EMAIL_EXISTED);
                });

        userRepository
                .findByUsername(newUser.getUsername())
                .ifPresent((u) -> {
                    throw new UsernameExistedException(Message.Exception.USERNAME_EXISTED);
                });

        User user = User
                .builder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .build();

        Role userRole = roleRepository
                        .findByName(URole.USER)
                        .orElseThrow(() -> new IllegalArgumentException(Message.Exception.ROLE_NOT_FOUND));

        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        log.info("User registered successfully: {}",newUser.getUsername());
    }

    @Override
    @Transactional
    public JwtResponseDTO login(LoginRequestDTO user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jWTUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return JwtResponseDTO
                .builder()
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public void logout() {
        Object principle = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        jWTUtils.getCleanJwtRefreshCookie();
        if (principle instanceof UserDetailsImpl user) {
            Long userId = user.getId();
            refreshTokenService.deleteByUserId(userId);
        }


    }

    @Override
    @Transactional
    public TokenRefreshResponseDTO refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RefreshTokenExpiredException(Message.Exception.REFRESH_TOKEN_EXPIRED);
        }
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String jwtCookie = jWTUtils.generateToken(user.getUsername());
                    return TokenRefreshResponseDTO
                            .builder()
                            .accessToken(jwtCookie)
                            .build();
                })
                .orElseThrow(() -> new RefreshTokenNotFoundException(Message.Exception.RESERVATION_NOT_FOUND));
    }
}
