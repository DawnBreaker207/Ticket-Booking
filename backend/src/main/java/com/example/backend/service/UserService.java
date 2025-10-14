package com.example.backend.service;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.User;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();

    User findOne(Long id);

    User update(Long id, User userDetails);

    User findByEmail(String email);


}
