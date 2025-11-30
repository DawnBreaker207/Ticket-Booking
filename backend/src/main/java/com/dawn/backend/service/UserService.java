package com.dawn.backend.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.response.UserResponseDTO;
import com.dawn.backend.model.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ResponsePage<UserResponseDTO> findAll(Pageable pageable);

    UserResponseDTO findOne(Long id);

    UserResponseDTO update(Long id, User userDetails);

    UserResponseDTO updateStatus(Long id, Boolean status);

    UserResponseDTO findByEmail(String email);
}
