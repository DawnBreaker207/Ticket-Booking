package com.example.backend.service;

import java.util.List;

import com.example.backend.model.User;

public interface UserService {
    List<User> findAll();

    User findOne(Long id);

    User update(Long id, User user);

    User findByEmail(String email);
    
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    User registerUser(User newUser);
}
