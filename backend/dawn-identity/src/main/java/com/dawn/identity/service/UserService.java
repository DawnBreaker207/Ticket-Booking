package com.dawn.identity.service;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.dto.request.UserRequest;
import com.dawn.backend.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ResponsePage<UserResponse> findAll(Pageable pageable);

    UserResponse findOne(Long id);

    UserResponse update(Long id, UserRequest userDetails);

    UserResponse updateStatus(Long id, Boolean status);

    UserResponse findByEmail(String email);
}
