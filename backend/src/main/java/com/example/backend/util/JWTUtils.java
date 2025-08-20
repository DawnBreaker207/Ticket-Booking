package com.example.backend.util;

import com.example.backend.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${dawn.app.jwtSecret}")
    private String jwtSecret;

    @Value("${dawn.app.jwtExpirationsMs}")
    private int jwtExpirations;

    @Value("${dawn.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${dawn.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;


    public ResponseCookie generateJwtCookie(UserDetailsImpl user) {
        String jwt = generateToken(user.getUsername());
        return generateCookie(jwtCookie, jwt, "/api/v1");
    }

    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateToken(user.getUsername());
        return generateCookie(jwtCookie, jwt, "/api/v1");
    }

    public ResponseCookie generateJwtRefreshCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookie, refreshToken, "/api/v1/refreshToken");
    }


    public String getJwtCookie(HttpServletRequest req) {
        return getCookieByName(req, jwtCookie);
    }

    public String getJwtRefreshCookie(HttpServletRequest req) {
        return getCookieByName(req, jwtRefreshCookie);
    }


    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtRefreshCookie, null).path("/api/v1").build();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, null).path("/api/v1/auth/refreshToken").build();
    }

    public String getUserNameFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String generateToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts
                .builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirations)))
                .signWith(key()).compact();
    }

    public String generateToken(String username) {

        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirations)))
                .signWith(key()).compact();
    }

    public SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException o) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token ");
        } catch (ExpiredJwtException o) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (UnsupportedJwtException o) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature");
        } catch (IllegalArgumentException o) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is empty or null");
        }
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).maxAge(20 * 60 * 60).httpOnly(true).build();
    }

    private String getCookieByName(HttpServletRequest req, String name) {
        Cookie cookie = WebUtils.getCookie(req, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
