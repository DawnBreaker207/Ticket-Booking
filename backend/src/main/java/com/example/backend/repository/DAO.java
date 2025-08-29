package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    int insert(T t);

    int update(T t);

    void delete(ID id);

}
