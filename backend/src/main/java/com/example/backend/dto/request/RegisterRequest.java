package com.example.backend.dto.request;

import java.util.HashSet;
import java.util.Set;

public class RegisterRequest {
    private String username;

    private String email;

    private Set<String> role;

    private String password;

    RegisterRequest(String username, String email, String password, Set<String> role) {
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
