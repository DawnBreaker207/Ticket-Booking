package com.example.backend.dto.request;

import jakarta.validation.constraints.*;

public class LoginRequestDTO {
    @NotBlank(message = "Username is not mandatory")
    @Size(min = 1, max = 100)
    private String username;

    @NotBlank(message = "Password is not mandatory")
    @Min(value = 6,message = "Password is required 8 characters above")
    private String password;

    public LoginRequestDTO(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
