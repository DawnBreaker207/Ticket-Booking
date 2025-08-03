package com.example.backend.service.Impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.Impl.UserRepositoryImpl;
import com.example.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepositoryImpl userRepository, BCryptPasswordEncoder passwordEncoder) {
	this.userRepository = userRepository;
	this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
	var users = userRepository.findAll();
	return users;
    }

    @Override
    public User findOne(Long id) {
	return userRepository.findOne(id).orElseThrow(() -> new UserNotFoundException("Can not find user with id " + id));

    }

    @Override
    public User update(Long id, User user) {
	var check = userRepository.findOne(id)
		.orElseThrow(() -> new UserNotFoundException("Can not find user with id " + id));

	check.setEmail(user.getEmail());
	check.setName(user.getName());
	check.setSurname(user.getSurname());

	return userRepository.save(check);

    }

    @Override
    public User registerUser(User newUser) {
	String encodedPassword = passwordEncoder.encode(newUser.getPassword());
	newUser.setPassword(encodedPassword);
	return userRepository.save(newUser);
    }

    @Override
    public User findByEmail(String email) {
	return userRepository.findByEmail(email)
		.orElseThrow(() -> new UserNotFoundException("Can not find user with email " + email));
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
	return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
