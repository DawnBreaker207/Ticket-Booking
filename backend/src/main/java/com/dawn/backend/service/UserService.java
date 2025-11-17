package com.dawn.backend.service;

import com.dawn.backend.dto.response.UserResponseDTO;
import com.dawn.backend.model.User;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();

    UserResponseDTO findOne(Long id);

    UserResponseDTO update(Long id, User userDetails);

    UserResponseDTO findByEmail(String email);


}
