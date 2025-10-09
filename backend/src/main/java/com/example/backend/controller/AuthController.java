package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.response.JwtResponseDTO;
import com.example.backend.dto.response.TokenRefreshResponseDTO;
import com.example.backend.service.AuthService;
import com.example.backend.util.JWTUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations related to auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JWTUtils jWTUtils;

    @PostMapping("/register")
    public ResponseObject<String> register(@RequestBody RegisterRequestDTO newUser) {
        authService.register(newUser);
        return new ResponseObject<>(HttpStatus.OK, "Success", "");
    }

    @PostMapping("/login")
    public ResponseObject<JwtResponseDTO> login(@RequestBody LoginRequestDTO user) {
        var jwt = authService.login(user);
        var refreshCookie = jWTUtils.generateJwtRefreshCookie(jwt.getRefreshToken());
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return new ResponseObject<>(HttpStatus.OK, "Success", jwt, header);

    }

    @PostMapping("/logout")
    public ResponseObject<?> logout() {
        authService.logout();
        return new ResponseObject<>(HttpStatus.NO_CONTENT, "Success", "");
    }

    @PostMapping("/refreshToken")
    public ResponseObject<TokenRefreshResponseDTO> refreshToken(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", authService.refreshToken(request));
    }
}
