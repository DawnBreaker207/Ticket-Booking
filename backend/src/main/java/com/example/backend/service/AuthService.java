package com.example.backend.service;

import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.response.JwtResponseDTO;
import com.example.backend.dto.response.TokenRefreshResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {


    void register(RegisterRequestDTO newUser);

    JwtResponseDTO login(LoginRequestDTO user);

    void logout();

    TokenRefreshResponseDTO refreshToken(HttpServletRequest req);
}
