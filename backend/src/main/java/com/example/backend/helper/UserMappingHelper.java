package com.example.backend.helper;

import com.example.backend.dto.shared.UserDTO;
import com.example.backend.model.User;

public interface UserMappingHelper {
    static UserDTO map(final User u) {
        UserDTO user = new UserDTO();
        user.setUserId(u.getId());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        return user;
    }

    static User map(final UserDTO u) {
        User user = new User();
        user.setId(u.getUserId());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        return user;
    }
}
