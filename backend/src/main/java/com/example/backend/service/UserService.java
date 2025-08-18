package com.example.backend.service;

import com.example.backend.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findOne(Long id);

    User update(Long id, User user);

    User findByEmail(String email);


}
