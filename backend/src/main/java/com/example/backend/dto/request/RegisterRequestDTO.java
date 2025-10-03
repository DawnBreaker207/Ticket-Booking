package com.example.backend.dto.request;

import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

public class RegisterRequestDTO {

    @NotBlank(message = "Username is not mandatory")
    @Size(min = 1, max = 100)
    private String username;


    @NotBlank(message = "Email is not mandatory")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Role is not mandatory")
    private Set<String> role;

    @NotBlank(message = "Password is not mandatory")
    @Min(value = 6,message = "Password is required 8 characters above ")
    private String password;

    RegisterRequestDTO(String username, String email, String password, Set<String> role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = new HashSet<>(role);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {return new HashSet<>(role);}

    public void setRole(Set<String> role) {this.role = new HashSet<>(role);}
}
