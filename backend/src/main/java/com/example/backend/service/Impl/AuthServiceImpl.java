package com.example.backend.service.Impl;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.JwtResponse;
import com.example.backend.exception.wrapper.UserEmailExistedException;
import com.example.backend.exception.wrapper.UsernameExistedException;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.Impl.RoleRepositoryImpl;
import com.example.backend.repository.Impl.UserRepositoryImpl;
import com.example.backend.service.AuthService;
import com.example.backend.util.JWTUtils;
import com.example.backend.util.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jWTUtils;

    public AuthServiceImpl(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtils jWTUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jWTUtils = jWTUtils;
    }

    @Override
    public void register(RegisterRequest newUser) {
        userRepository.findByEmail(newUser.getEmail()).ifPresent(u -> {
            throw new UserEmailExistedException("This email already exists" + newUser.getEmail());
        });
        userRepository.findByUsername(newUser.getUsername()).ifPresent((u) -> {
            throw new UsernameExistedException("This username already exists" + newUser.getUsername());
        });
        var user = new User();
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Set<Role> roles = newUser
                .getRole()
                .stream()
                .map(roleName -> roleRepository
                        .findByName(roleName.toUpperCase())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found " + roleName))

                )
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jWTUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
