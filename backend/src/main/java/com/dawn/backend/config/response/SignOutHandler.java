package com.dawn.backend.config.response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SignOutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        ResponseCookie cleanJwtCookie = ResponseCookie
                .from("jwt", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
