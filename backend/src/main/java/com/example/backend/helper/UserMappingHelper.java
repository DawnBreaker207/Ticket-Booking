package com.example.backend.helper;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.User;

public interface UserMappingHelper {
    static UserResponseDTO map(final User u) {
        UserResponseDTO user = new UserResponseDTO();
        user.setUserId(u.getId());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        return user;
    }

    static User map(final UserResponseDTO u) {
        User user = new User();
        user.setId(u.getUserId());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        return user;
    }
}
