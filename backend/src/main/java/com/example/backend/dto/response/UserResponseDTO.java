package com.example.backend.dto.response;

import com.example.backend.model.AbstractMappedEntity;

public class UserResponseDTO extends AbstractMappedEntity {

    private Long userId;
    private String username;
    private String email;
    private String password;

    public UserResponseDTO() {
        super();
    }

    public UserResponseDTO(String username, Long userId, String email, String password) {
        super();
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
