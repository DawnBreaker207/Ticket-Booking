package com.example.backend.service.Impl;

import com.example.backend.exception.wrapper.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.Impl.UserRepositoryImpl;
import com.example.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepository;


    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();

    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOne(id).orElseThrow(() -> new UserNotFoundException("Can not find user with id " + id));

    }

    @Override
    public User update(Long id, User user) {
        var check = userRepository.findOne(id).orElseThrow(() -> new UserNotFoundException("Can not find user with id " + id));

        check.setEmail(user.getEmail());
        check.setUsername(user.getUsername());

        return userRepository.save(check);

    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Can not find user with email " + email));
    }


}
