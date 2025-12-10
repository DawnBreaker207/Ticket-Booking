package com.dawn.identity.service;

import com.dawn.backend.dto.request.LoginRequest;
import com.dawn.backend.dto.request.RegisterRequest;
import com.dawn.backend.dto.response.JwtResponse;
import com.dawn.backend.dto.response.TokenRefreshResponse;

public interface AuthService {

    void register(RegisterRequest newUser);

    JwtResponse login(LoginRequest user);

    TokenRefreshResponse refreshToken(String refreshToken);
}
