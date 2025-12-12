package com.dawn.identity.service;

import com.dawn.api.identity.service.UserClientService;
import com.dawn.common.dto.response.ResponsePage;
import com.dawn.identity.dto.request.UserRequest;
import com.dawn.identity.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService extends UserClientService {
    ResponsePage<UserResponse> findAll(Pageable pageable);

    UserResponse findOne(Long id);

    UserResponse update(Long id, UserRequest userDetails);

    UserResponse updateStatus(Long id, Boolean status);

    UserResponse findByEmail(String email);
}
