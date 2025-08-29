package com.example.backend.repository;

import com.example.backend.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface RoleRepository extends DAO<Role, Long> {
    Optional<Role> findByName(String name);
}
