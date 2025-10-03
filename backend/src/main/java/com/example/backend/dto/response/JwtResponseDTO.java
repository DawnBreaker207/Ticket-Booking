package com.example.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponseDTO(String token, Long userId, String username, String email, List<String> roles, String refreshToken) {
        this.roles = new ArrayList<>(roles) ;
        this.email = email;
        this.username = username;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public List<String> getRoles() {
        return new ArrayList<>(roles);
    }

    public void setRoles(List<String> roles) {
        this.roles = new ArrayList<>(roles);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
