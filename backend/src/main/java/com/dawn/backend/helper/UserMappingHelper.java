package com.dawn.backend.helper;

import com.dawn.backend.dto.response.UserResponseDTO;
import com.dawn.backend.model.User;

public interface UserMappingHelper {
    static UserResponseDTO map(final User u) {
        return UserResponseDTO
                .builder()
                .userId(u.getId())
                .email(u.getEmail())
                .username(u.getUsername())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }

    static User map(final UserResponseDTO u) {
        return User
                .builder()
                .id(u.getUserId())
                .email(u.getEmail())
                .username(u.getUsername())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}
