package com.dawn.identity.service.Impl;

import com.dawn.common.constant.Message;
import com.dawn.common.constant.URole;
import com.dawn.common.exception.wrapper.ResourceAlreadyExistedException;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import com.dawn.identity.dto.request.LoginRequest;
import com.dawn.identity.dto.request.RegisterRequest;
import com.dawn.identity.dto.response.JwtResponse;
import com.dawn.identity.dto.response.TokenRefreshResponse;
import com.dawn.identity.model.RefreshToken;
import com.dawn.identity.model.Role;
import com.dawn.identity.model.User;
import com.dawn.identity.model.UserDetailsImpl;
import com.dawn.identity.repository.RoleRepository;
import com.dawn.identity.repository.UserRepository;
import com.dawn.identity.service.AuthService;
import com.dawn.identity.service.RefreshTokenService;
import com.dawn.identity.utils.JWTUtils;
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
    public void register(RegisterRequest newUser) {

        userRepository
                .findByEmail(newUser.getEmail())
                .ifPresent(u -> {
                    throw new ResourceAlreadyExistedException(Message.Exception.EMAIL_EXISTED);
                });

        userRepository
                .findByUsername(newUser.getUsername())
                .ifPresent((u) -> {
                    throw new ResourceAlreadyExistedException(Message.Exception.USERNAME_EXISTED);
                });

        User user = User
                .builder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .build();

        Role userRole = roleRepository
                .findByName(URole.USER)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.ROLE_NOT_FOUND));

        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        log.info("User registered successfully: {}", newUser.getUsername());
    }

    @Override
    @Transactional
    public JwtResponse login(LoginRequest user) {
        String identifier = user.getIdentifier();

        //   Detect email
        String loginKey = identifier.contains("@")
                ? userRepository
                .findByEmail(identifier)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Message.Exception.EMAIL_NOT_FOUND))
                .getEmail()
                : identifier;
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginKey, user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jWTUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return JwtResponse
                .builder()
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ResourceNotFoundException(Message.Exception.REFRESH_TOKEN_EXPIRED);
        }
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String jwtCookie = jWTUtils.generateToken(user.getUsername());
                    return TokenRefreshResponse
                            .builder()
                            .accessToken(jwtCookie)
                            .build();
                })
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.REFRESH_TOKEN_NOT_FOUND));
    }
}
