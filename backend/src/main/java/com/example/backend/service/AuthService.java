package com.example.backend.service;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.dto.response.TokenRefreshResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {


    void register(RegisterRequest newUser);

    JwtResponse login(LoginRequest user);

    void logout();

    TokenRefreshResponse refreshToken(HttpServletRequest req);
}
