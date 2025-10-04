package com.example.backend.repository;

import com.example.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u SET u.username = :#{#user.username}, u.email = :#{#user.email}, u.password = :#{#user.password} ", nativeQuery = true)
    int update(User user);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_id) VALUES (:userId, :roleId})", nativeQuery = true)
    void insertUserRoles( Long userId, Long roleId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
