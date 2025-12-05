package com.dawn.backend.service.Impl;

import com.dawn.backend.config.response.ResponsePage;
import com.dawn.backend.constant.Message;
import com.dawn.backend.dto.request.UserRequest;
import com.dawn.backend.dto.response.UserResponse;
import com.dawn.backend.exception.wrapper.ResourceNotFoundException;
import com.dawn.backend.helper.UserMappingHelper;
import com.dawn.backend.repository.UserRepository;
import com.dawn.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String USER_CACHE = "user";
    private final UserRepository userRepository;

    @Override
//    @Cacheable(value = USER_CACHE)
    public ResponsePage<UserResponse> findAll(Pageable pageable) {
        return ResponsePage.of(
                userRepository
                        .findAll(pageable)
                        .map(UserMappingHelper::map));
    }

    @Override
    @Cacheable(value = USER_CACHE, key = "'id:' + #id")
    public UserResponse findOne(Long id) {
        return userRepository
                .findById(id)
                .map(UserMappingHelper::map)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.USER_NOT_FOUND));
    }

    @Override
    @Cacheable(value = USER_CACHE, key = "'email:' + #email")
    public UserResponse findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(UserMappingHelper::map)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    @CachePut(value = USER_CACHE, key = "'id:' + #id")
    public UserResponse update(Long id, UserRequest userDetails) {
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.USER_NOT_FOUND));
        user.setUsername(userDetails.getUsername());
        user.setAvatar(userDetails.getAvatar());
        return UserMappingHelper.map(userRepository.save(user));
    }

    @Override
    @Transactional
    @CacheEvict(value = USER_CACHE, key = "'id:' + #id + 'status' + #status")
    public UserResponse updateStatus(Long id, Boolean status) {
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.Exception.USER_NOT_FOUND));
        user.setIsDeleted(status);
        return UserMappingHelper.map(userRepository.save(user));
    }
}
