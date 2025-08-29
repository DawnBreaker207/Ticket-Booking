package com.example.backend.service.Impl;

import com.example.backend.exception.wrapper.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "Can not find user with id " + id));
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        var check = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "Can not find user with id " + id));
        check.setEmail(user.getEmail());
        check.setUsername(user.getUsername());
        userRepository.save(check);
        return check;

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "Can not find user with email " + email));
    }
}
