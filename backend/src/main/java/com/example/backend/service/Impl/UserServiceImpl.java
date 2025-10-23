package com.example.backend.service.Impl;

import com.example.backend.constant.Message;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.exception.wrapper.UserNotFoundException;
import com.example.backend.helper.UserMappingHelper;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String USER_CACHE = "user";
    private final UserRepository userRepository;

    @Override
    @Cacheable(value = USER_CACHE)
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(UserMappingHelper::map)
                .toList();
    }

    @Override
    @Cacheable(value = USER_CACHE, key = "'id:' + #id")
    public UserResponseDTO findOne(Long id) {
        return userRepository
                .findById(id)
                .map(UserMappingHelper::map)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    @CachePut(value = USER_CACHE, key = "'id:' + #id")
    public UserResponseDTO update(Long id, User userDetails) {
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.USER_NOT_FOUND));
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.markUpdated();
        return UserMappingHelper.map(userRepository.save(user));
    }

    @Override
    @Cacheable(value = USER_CACHE, key = "'email:' + #email")
    public UserResponseDTO findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(UserMappingHelper::map)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, Message.Exception.USER_NOT_FOUND));
    }
}
