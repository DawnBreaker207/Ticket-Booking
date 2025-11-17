package com.dawn.backend.repository;

import com.dawn.backend.constant.URole;
import com.dawn.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(URole name);
}
