package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    default List<T> findAll() {
	return null;
    }

    default Optional<T> findOne(Long id) {
	return null;
    }

    default Optional<T> findOne(String id) {
	return null;
    }

    default T save(T t) {
	return null;
    }

    default T update(T t) {
	return null;
    }

    default void delete(Long id) {
	return;
    }

    default void delete(String id) {
	return;
    }
}
