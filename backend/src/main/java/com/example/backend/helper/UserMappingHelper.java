package com.example.backend.helper;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.Movie;
import com.example.backend.model.User;

public interface UserMappingHelper {
    static UserResponseDTO map(final User u) {
        return UserResponseDTO.builder()
                .userId(u.getId())
                .email(u.getEmail())
                .password(u.getPassword())
                .build();
    }

    static User map(final UserResponseDTO u) {
        return User.builder()
                .id(u.getUserId())
                .email(u.getEmail())
                .password(u.getPassword())
                .build();
    }
}
