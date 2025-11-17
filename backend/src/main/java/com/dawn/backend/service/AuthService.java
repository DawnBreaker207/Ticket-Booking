package com.dawn.backend.service;

import com.dawn.backend.dto.request.LoginRequestDTO;
import com.dawn.backend.dto.request.RegisterRequestDTO;
import com.dawn.backend.dto.response.JwtResponseDTO;
import com.dawn.backend.dto.response.TokenRefreshResponseDTO;

public interface AuthService {


    void register(RegisterRequestDTO newUser);

    JwtResponseDTO login(LoginRequestDTO user);

    void logout();

    TokenRefreshResponseDTO refreshToken(String refreshToken);
}
