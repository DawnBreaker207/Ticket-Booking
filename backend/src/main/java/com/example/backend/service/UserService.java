package com.example.backend.service;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.User;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();

    UserResponseDTO findOne(Long id);

    UserResponseDTO update(Long id, User userDetails);

    UserResponseDTO findByEmail(String email);


}
