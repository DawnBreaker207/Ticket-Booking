package com.example.backend.service.Impl;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.exception.wrapper.UserEmailExistedException;
import com.example.backend.exception.wrapper.UsernameExistedException;
import com.example.backend.model.User;
import com.example.backend.repository.Impl.UserRepositoryImpl;
import com.example.backend.service.AuthService;
import com.example.backend.util.JWTUtils;
import com.example.backend.util.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryImpl userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jWTUtils;

    public AuthServiceImpl(UserRepositoryImpl userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtils jWTUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jWTUtils = jWTUtils;
    }

    @Override
    public void registerUser(RegisterRequest newUser) {
        userRepository.findByEmail(newUser.getEmail()).ifPresent(u -> {
            throw new UserEmailExistedException("This email already exists" + newUser.getEmail());
        });
        userRepository.findByUsername(newUser.getUsername()).ifPresent((u) -> {
            throw new UsernameExistedException("This username already exists" + newUser.getUsername());
        });
        var user = new User();
        user.setName(newUser.getUsername());
        user.setSurname(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jWTUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail());
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
