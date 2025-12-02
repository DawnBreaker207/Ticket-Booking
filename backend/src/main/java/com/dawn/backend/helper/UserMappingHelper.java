package com.dawn.backend.helper;

import com.dawn.backend.dto.response.UserResponse;
import com.dawn.backend.model.User;

public interface UserMappingHelper {
    static UserResponse map(final User u) {
        return UserResponse
                .builder()
                .userId(u.getId())
                .email(u.getEmail())
                .username(u.getUsername())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .isDeleted(u.getIsDeleted())
                .build();
    }

    static User map(final UserResponse u) {
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
