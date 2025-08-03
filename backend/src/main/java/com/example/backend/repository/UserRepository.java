package com.example.backend.repository;

import java.util.Optional;

import com.example.backend.model.User;

public interface UserRepository extends DAO<User> {
    Optional<User> findByEmail(String email);
}
