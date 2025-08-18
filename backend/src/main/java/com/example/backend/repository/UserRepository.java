package com.example.backend.repository;

import com.example.backend.model.User;

import java.util.Optional;

public interface UserRepository extends DAO<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
