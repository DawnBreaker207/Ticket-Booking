package com.example.backend.repository;

import com.example.backend.model.Role;

import java.util.Optional;

public interface RoleRepository extends DAO<Role> {
    Optional<Role> findByName(String name);
}
