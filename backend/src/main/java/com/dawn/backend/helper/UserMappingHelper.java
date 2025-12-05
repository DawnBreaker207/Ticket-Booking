package com.dawn.backend.helper;

import com.dawn.backend.dto.request.UserRequest;
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
                .avatar(u.getAvatar())
                .role(u.getRoles().toString())
                .isDeleted(u.getIsDeleted())
                .build();
    }

    static User map(final UserRequest u) {
        return User
                .builder()
                .username(u.getUsername())
                .avatar(u.getAvatar())
                .build();
    }
}
