package com.example.backend.service;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.JwtResponse;

public interface AuthService {

    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    void register(RegisterRequest newUser);

    JwtResponse login(LoginRequest user);
}
