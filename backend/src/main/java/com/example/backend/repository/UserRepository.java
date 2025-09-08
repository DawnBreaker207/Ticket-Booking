package com.example.backend.repository;

import com.example.backend.model.Role;
import com.example.backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
@Repository
public interface UserRepository extends DAO<User, Long> {

    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(Long aLong);

    @Override
    int insert(User user);

    @Override
    int update(User user);

    default User save(User input) {
        if (input.getId() == null) {
            insert(input);
        } else {
            update(input);
        }
        return input;
    }

    @Override
    void delete(Long aLong);

    void insertUserRoles(@Param("userId") Long userId, @Param("roles") Set<Role> roles);


    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUsername(@Param("username") String username);

}
