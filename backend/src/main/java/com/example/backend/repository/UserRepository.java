package com.example.backend.repository;

import com.example.backend.constant.URole;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("""
        SELECT CASE
            WHEN COUNT(u) > 0
            THEN true ELSE false END
        FROM User AS u JOIN u.roles AS r
        WHERE r.name = :roleName
        """)
    boolean existsByRolesName(URole roleName);
}
