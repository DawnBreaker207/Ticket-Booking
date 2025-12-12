package com.dawn.identity.helper;


import com.dawn.api.identity.dto.UserDTO;
import com.dawn.identity.dto.request.UserRequest;
import com.dawn.identity.dto.response.UserResponse;
import com.dawn.identity.model.User;

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

    static UserDTO mapDto(final User u) {
        return UserDTO
                .builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
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
