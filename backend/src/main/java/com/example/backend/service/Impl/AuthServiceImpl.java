package com.example.backend.service.Impl;

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
import com.example.backend.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jWTUtils;
    private final RefreshTokenServiceImpl refreshTokenService;


    @Override
    @Transactional
    public void register(RegisterRequestDTO newUser) {
        userRepository.findByEmail(newUser.getEmail()).ifPresent(u -> {
            throw new UserEmailExistedException("This email already exists " + newUser.getEmail());
        });
        userRepository.findByUsername(newUser.getUsername()).ifPresent((u) -> {
            throw new UsernameExistedException("This username already exists " + newUser.getUsername());
        });
        var user = new User();
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));


        Set<Role> roles = newUser
                .getRole()
                .stream()
                .map(roleName -> roleRepository
                        .findByName(URole.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> new IllegalArgumentException("Role not found " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public JwtResponseDTO login(LoginRequestDTO user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jWTUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return JwtResponseDTO
                .builder()
                .token(jwt)
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public void logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle instanceof UserDetailsImpl user) {
            Long userId = user.getId();
            refreshTokenService.deleteByUserId(userId);
        }


    }

    @Override
    @Transactional
    public TokenRefreshResponseDTO refreshToken(HttpServletRequest token) {
        String refreshToken = jWTUtils.getJwtRefreshCookie(token);
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RefreshTokenExpiredException("Refresh token not existed");
        }
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String jwtCookie = jWTUtils.generateToken(user.getUsername());
                    return TokenRefreshResponseDTO
                            .builder()
                            .accessToken(jwtCookie)
                            .refreshToken(refreshToken)
                            .build();
                })
                .orElseThrow(() -> new RefreshTokenNotFoundException("Token not found " + token));
    }
}
