package com.dawn.backend.service.Impl;

import com.dawn.backend.constant.Message;
import com.dawn.backend.model.User;
import com.dawn.backend.model.UserDetailsImpl;
import com.dawn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(Message.Exception.USERNAME_NOT_FOUND));
        return UserDetailsImpl.build(user);
    }
}
