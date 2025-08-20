package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.dto.response.TokenRefreshResponse;
import com.example.backend.service.Impl.AuthServiceImpl;
import com.example.backend.util.JWTUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations related to auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private JWTUtils jWTUtils;

    @PostMapping("/register")
    public ResponseObject<String> register(@RequestBody RegisterRequest newUser) {
        authService.register(newUser);
        return new ResponseObject<>(HttpStatus.OK, "Success", "");
    }

    @PostMapping("/login")
    public ResponseObject<JwtResponse> login(@RequestBody LoginRequest user) {
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
    public ResponseObject<TokenRefreshResponse> refreshToken(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", authService.refreshToken(request));
    }
}
