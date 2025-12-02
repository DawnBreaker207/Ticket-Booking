package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.dto.request.LoginRequest;
import com.dawn.backend.dto.request.RegisterRequest;
import com.dawn.backend.dto.response.JwtResponse;
import com.dawn.backend.dto.response.TokenRefreshResponse;
import com.dawn.backend.service.AuthService;
import com.dawn.backend.util.JWTUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations related to auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JWTUtils jWTUtils;

    @PostMapping("/register")
    public ResponseObject<String> register(@RequestBody RegisterRequest newUser) {
        authService.register(newUser);
        return new ResponseObject<>(HttpStatus.OK, "Success", "");
    }

    @PostMapping("/login")
    public ResponseObject<JwtResponse> login(@RequestBody LoginRequest user) {
        JwtResponse jwt = authService.login(user);
        return new ResponseObject<>(
                HttpStatus.OK,
                "Success",
                jwt,
                new HttpHeaders() {{
                    add(
                            HttpHeaders.SET_COOKIE,
                            jWTUtils.generateJwtRefreshCookie(jwt.getRefreshToken())
                                    .toString());
                }}
        );
    }

    @PostMapping("/refresh-token")
    public ResponseObject<TokenRefreshResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        TokenRefreshResponse token = authService.refreshToken(refreshToken);
        return new ResponseObject<>(HttpStatus.OK, "Success", token,
                new HttpHeaders() {{
                    add(
                            HttpHeaders.SET_COOKIE,
                            jWTUtils.generateJwtRefreshCookie(token.getRefreshToken())
                                    .toString());
                }});
    }
}
