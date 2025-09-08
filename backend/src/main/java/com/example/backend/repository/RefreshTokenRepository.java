package com.example.backend.repository;

import com.example.backend.model.RefreshToken;
import com.example.backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Mapper
@Repository
public interface RefreshTokenRepository extends DAO<RefreshToken, String> {

    @Override
    List<RefreshToken> findAll();

    @Override
    Optional<RefreshToken> findById(String s);

    @Override
    int insert(RefreshToken refreshToken);

    @Override
    int update(RefreshToken refreshToken);

    default RefreshToken save(RefreshToken input) {
        if (input.getId() == null) {
            insert(input);
        } else {
            update(input);
        }
        return input;
    }

    @Override
    void delete(String s);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
